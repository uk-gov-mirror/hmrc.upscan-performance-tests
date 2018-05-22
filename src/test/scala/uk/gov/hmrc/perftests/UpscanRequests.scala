package uk.gov.hmrc.perftests

import io.gatling.commons.util.ClockSingleton
import io.gatling.core.Predef._
import io.gatling.core.action.builder.SessionHookBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import org.json4s._
import org.json4s.native.JsonMethods._
import uk.gov.hmrc.performance.conf.{HttpConfiguration, ServicesConfiguration}

import scala.concurrent.duration._

object UpscanRequests extends ServicesConfiguration with HttpConfiguration {

  private val upscanBaseUrl        = baseUrlFor("upscan") + "/upscan"
  private val upscaListenerBaseUrl = baseUrlFor("upscan-listener") + "/upscan-listener"

  val callBackUrl = "https://upscan-listener.public.mdtp/upscan-listener/listen"

  private val fileSize = readProperty("journeys.upscan.fileSize").toInt

  private val pollingTimeout = readProperty("journeys.upscan.pollingTimeoutInSeconds").toInt.seconds

  val fileBody: Array[Byte] = Array.fill[Byte](fileSize)(0)

  val initiateTheUpload: HttpRequestBuilder =
    http("Initiate file upload")
      .post(s"$upscanBaseUrl/initiate")
      .header("User-Agent", "upscan-performance-tests")
      .body(
        StringBody(s"""{ "callbackUrl": "$callBackUrl" }""")
      )
      .asJSON
      .check(status.is(200))
      .check(bodyString.saveAs("initiateResponse"))

  case class PreparedUpload(reference: String, uploadRequest: UploadFormTemplate)

  case class UploadFormTemplate(href: String, fields: Map[String, String])

  val parseInitiateResponse = new SessionHookBuilder(
    (session: Session) => {
      if (session.isFailed) {
        session
      } else {
        implicit val formats = DefaultFormats

        val initiateResponse   = session.attributes("initiateResponse").toString
        val uploadFormTemplate = parse(initiateResponse).extract[PreparedUpload]
        session
          .set("uploadHref", uploadFormTemplate.uploadRequest.href)
          .set("fields", uploadFormTemplate.uploadRequest.fields)
          .set("reference", uploadFormTemplate.reference)
      }
    }
  )

  val generateFileBody: SessionHookBuilder = new SessionHookBuilder(
    (session: Session) => {
      session.set("fileBody", fileBody)
    }
  )

  val uploadFileToAws: HttpRequestBuilder = http("Uploading file to AWS")
    .post("${uploadHref}")
    .asMultipartForm
    .bodyPart(StringBodyPart("x-amz-meta-callback-url", "${fields.x-amz-meta-callback-url}"))
    .bodyPart(StringBodyPart("x-amz-meta-consuming-service", "${fields.x-amz-meta-consuming-service}"))
    .bodyPart(StringBodyPart("x-amz-date", "${fields.x-amz-date}"))
    .bodyPart(StringBodyPart("x-amz-credential", "${fields.x-amz-credential}"))
    .bodyPart(StringBodyPart("x-amz-algorithm", "${fields.x-amz-algorithm}"))
    .bodyPart(StringBodyPart("key", "${fields.key}"))
    .bodyPart(StringBodyPart("acl", "${fields.acl}"))
    .bodyPart(StringBodyPart("x-amz-signature ", "${fields.x-amz-signature}"))
    .bodyPart(StringBodyPart("policy", "${fields.policy}"))
    .bodyPart(ByteArrayBodyPart("file", "${fileBody}"))
    .check(status.is(204))

  val registerPoolLoopStartTime = new SessionHookBuilder(
    (session: Session) => {
      session.set("loopStartTime", ClockSingleton.nowMillis)
    }
  )

  val pollStatusUpdates =
    asLongAs(
      conditionOrTimeout(session => !session.attributes.get("status").contains(200), "loopStartTime", pollingTimeout)) {
      exec(
        http("Polling file processing status")
          .get(s"$upscaListenerBaseUrl/poll/" + "${reference}")
          .check(status.in(200, 404).saveAs("status"))
          .silent).pause(500 milliseconds)
    }.actionBuilders

  private def conditionOrTimeout(condition: Session => Boolean, loopTimer: String, timeout: Duration)(
    session: Session) =
    condition(session) &&
      (ClockSingleton.nowMillis - session.attributes(loopTimer).asInstanceOf[Long]) < timeout.toMillis

  val finalCheckForProcessingStatus: HttpRequestBuilder = http("Verifying final file processing status")
    .get(s"$upscaListenerBaseUrl/poll/" + "${reference}")
    .check(status.is(200))
}
