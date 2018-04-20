package uk.gov.hmrc.perftests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.UpscanRequests._

class Simulations extends PerformanceTestRunner {

  setup("upscan", "Upscan successful file upload")
    .withRequests(initiateTheUpload)
    .withActions(parseInitiateResponse, generateFileBody)
    .withActions(uploadFileToAws)

  runSimulation()
}
