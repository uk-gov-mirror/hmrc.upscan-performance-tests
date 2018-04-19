package uk.gov.hmrc.perftests

import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.perftests.UpscanRequests._

object Parts {

  val upscan: Seq[HttpRequestBuilder] = Seq(upscanInitiate, fileUploadToAWS, pollListener)
}