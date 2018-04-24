package uk.gov.hmrc.perftests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.UpscanRequests._

class Simulations extends PerformanceTestRunner {

  setup("upscan", "Upscan successful file upload")
    .withActions(initiateTheUpload, parseInitiateResponse, generateFileBody, uploadFileToAws)
    .withActions(pollForResult: _*)
  runSimulation()
}
