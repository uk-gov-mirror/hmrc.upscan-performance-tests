import sbt._

object Dependencies {

  val test = Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.14.0" % Test,
  "io.gatling" % "gatling-test-framework" % "2.3.1" % Test,
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.1" % Test,
  "com.typesafe" % "config" % "1.3.0" % Test,
  "uk.gov.hmrc" %% "performance-test-runner" % "3.7.0" % Test,
  "io.gatling.vtd" % "gatling-vtd" % "2.2.0" % Test,
  "org.json4s" %% "json4s-jackson" % "3.2.11" % Test,
  "org.json4s" %% "json4s-native" % "3.2.11" % Test
  )
}
