package uk.gov.hmrc.perftests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.UpscanRequests._

class Simulations extends PerformanceTestRunner {

  setup("v1-clean-pdf", "V1 Upload clean pdf")
    .withActions(initiateTheUploadV1, parseInitiateResponse, addFileToSession("/upload/test.pdf"), uploadFileToAws)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("READY"))

  setup("v1-virus", "V1 Upload virus")
    .withActions(
      initiateTheUploadV1, parseInitiateResponse, addFileToSession("/upload/eicar-standard-av-test-file"), uploadFileToAws)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("FAILED"))

  setup("v1-invalid-txt-filetype", "V1 Upload invalid .txt file type")
    .withActions(initiateTheUploadV1, parseInitiateResponse, addFileToSession("/upload/test.txt"), uploadFileToAws)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("FAILED"))

  setup("v2-clean-pdf", "V2 Upload clean pdf")
    .withActions(initiateTheUploadV2, parseInitiateResponse, addFileToSession("/upload/test.pdf"), uploadFileToUpscanProxy)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("READY"))

  setup("v2-virus", "V2 Upload virus")
    .withActions(
      initiateTheUploadV2, parseInitiateResponse, addFileToSession("/upload/eicar-standard-av-test-file"), uploadFileToUpscanProxy)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("FAILED"))

  setup("v2-invalid-txt-filetype", "V2 Upload invalid .txt file type")
    .withActions(initiateTheUploadV2, parseInitiateResponse, addFileToSession("/upload/test.txt"), uploadFileToUpscanProxy)
    .withActions(registerPoolLoopStartTime)
    .withActions(pollStatusUpdates: _*)
    .withActions(verifyFileStatus("FAILED"))

  runSimulation()
}
