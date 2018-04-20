package uk.gov.hmrc.perftests

import io.gatling.core.Predef._
import io.gatling.core.action.builder.SessionHookBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.{HttpConfiguration, ServicesConfiguration}
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.util.Random

object UpscanRequests extends ServicesConfiguration with HttpConfiguration {

  private val baseUrl = baseUrlFor("upscan")

  val callBackUrl = "http://notFound.com"

  private val maxFileSize = 10 * 1024 * 1024

  val initiateTheUpload: HttpRequestBuilder =
    http("Upscan Initiate")
      .post(s"$baseUrl/initiate")
      .body(
        StringBody(s"""{ "callbackUrl": "$callBackUrl" }""")
      )
      .asJSON
      .check(jsonPath("$.uploadRequest").find.saveAs("initiateResponse"))
      .check(status.is(200))

  case class UploadFormTemplate(href: String, fields: Map[String, String])

  val parseInitiateResponse = new SessionHookBuilder(
    (session: Session) => {
      implicit val formats = DefaultFormats

      val initiateResponse   = session.attributes("initiateResponse").toString
      val uploadFormTemplate = parse(initiateResponse).extract[UploadFormTemplate]
      session
        .set("uploadHref", uploadFormTemplate.href)
        .set("fields", uploadFormTemplate.fields)
    }
  )

  val generateFileBody: SessionHookBuilder = new SessionHookBuilder(
    (session: Session) => {
      val fileBody: Array[Byte] = Array.fill[Byte](Random.nextInt(maxFileSize))(0)
      session.set("fileBody", fileBody)
    }
  )

  val uploadFileToAws: HttpRequestBuilder = http("Uploading file to AWS")
    .post("${uploadHref}")
    .asMultipartForm
    .bodyPart(StringBodyPart("x-amz-meta-callback-url", "${fields.x-amz-meta-callback-url}"))
    .bodyPart(StringBodyPart("x-amz-date", "${fields.x-amz-date}"))
    .bodyPart(StringBodyPart("x-amz-credential", "${fields.x-amz-credential}"))
    .bodyPart(StringBodyPart("x-amz-algorithm", "${fields.x-amz-algorithm}"))
    .bodyPart(StringBodyPart("key", "${fields.key}"))
    .bodyPart(StringBodyPart("acl", "${fields.acl}"))
    .bodyPart(StringBodyPart("x-amz-signature ", "${fields.x-amz-signature}"))
    .bodyPart(StringBodyPart("policy", "${fields.policy}"))
    .bodyPart(ByteArrayBodyPart("file", "${fileBody}"))
    .check(status.is(204))
}
