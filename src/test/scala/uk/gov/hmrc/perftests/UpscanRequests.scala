package uk.gov.hmrc.perftests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object UpscanRequests{

  val initiateUrl = "http://www.staging.tax.service.gov.uk/upscan/initiate"

  val callBackUrl = ""

  val minimumfileSize = 0

  val maximumFileSize = 70000

  val mimeType = ""

  val upscanInitiate: HttpRequestBuilder = {
    http("Upscan Initiate")
      .post(initiateUrl)
      .header("Content-Type", "application/json")
      .formParam("callbackUrl", callBackUrl)
      .formParam("minimumFileSize", minimumfileSize)
      .formParam("maximumFileSize", maximumFileSize)
      .formParam("expectedMimeType",mimeType)
      .check(jsonPath("$.uploadRequest").find.saveAs("preparedUpload"))
      .check(status.is(200))
  }

  val fileUploadToAWS: HttpRequestBuilder = {
    http("Uploading file to AWS")
      .post("${preparedUpload.href}")
      .check(status.is(203))
  }
}
