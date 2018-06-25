package uk.gov.hmrc.perftests

import io.gatling.commons.stats.KO
import io.gatling.http.request.ExtraInfo

class ExtraInfoExtractor {
  def dumpOnFailure(extraInfo: ExtraInfo): List[Any] = {
    if (extraInfo.status.eq(KO)) {
      List(s"Status Code: [${extraInfo.response.statusCode}]. " +
        s"Response Header: [${extraInfo.response.headers}]. " +
        s"Response: [${extraInfo.response.body}]. \n")
    }else {
      List(s"\nAll is Well\n")
    }
  }
}

object ExtraInfoExtractor extends ExtraInfoExtractor