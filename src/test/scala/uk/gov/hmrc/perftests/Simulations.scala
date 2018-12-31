package uk.gov.hmrc.perftests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.UpscanRequests._

class Simulations extends PerformanceTestRunner {

  setup("clean-pdf", "Upload clean pdf")
    .withActions(initiateTheUpload, parseInitiateResponse, addFileToSession("/upload/test.pdf"), uploadFileToAws)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("READY"))

  setup("virus", "Upload virus")
    .withActions(initiateTheUpload, parseInitiateResponse, addFileToSession("/upload/eicar-standard-av-test-file"), uploadFileToAws)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("FAILED"))

  setup("invalid-txt-filetype", "Upload invalid .txt file type")
    .withActions(initiateTheUpload, parseInitiateResponse, addFileToSession("/upload/test.txt"), uploadFileToAws)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("FAILED"))

  runSimulation()
}
