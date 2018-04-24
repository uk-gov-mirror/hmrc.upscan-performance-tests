import sbt._

object Dependencies {

  object Compile {
    val nscalaTime = "com.github.nscala-time" %% "nscala-time" % "2.2.0"
    val gatlingTestFramework = "io.gatling" % "gatling-test-framework" % "2.2.5"
    val gatlingHighCharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.5"
    val typesafeConfig = "com.typesafe" % "config" % "1.3.0"
    val performanceTestRunner = "uk.gov.hmrc" %% "performance-test-runner" % "3.1.0"
    val gatlingVTDXMLPlugin = "io.gatling.vtd" % "gatling-vtd" % "2.2.0"
    val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.2.10"
    val json4sNative = "org.json4s" %% "json4s-native" % "3.2.10"
  }
}
