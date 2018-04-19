package uk.gov.hmrc.perftests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner

class Simulations extends PerformanceTestRunner {

  setup("upscan", "Upscan Initiate Request") withRequests (Parts.upscan: _*)
  runSimulation()
}
