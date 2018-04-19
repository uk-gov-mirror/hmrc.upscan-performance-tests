import io.gatling.sbt.GatlingPlugin
import sbt.Keys._
import sbt._
import sbt.plugins.{CorePlugin, IvyPlugin, JvmPlugin}

import scala.util.Properties._

object Build extends Build {

  val appName = "upscan-performance-tests"

  val scalaOptions = Seq(
    "-unchecked",
    "-deprecation",
    "-Xlint",
    "-language:_",
    "-target:jvm-1.8",
    "-Xmax-classfile-name", "100",
    "-encoding", "UTF-8"
  )

  val projectSettings = Seq(
    organization := "uk.gov.hmrc",
    version := envOrElse("UPSCAN_PERFORMANCE_TESTS_VERSION", "999-SNAPSHOT"),
    scalaVersion := "2.11.7"
  )

  lazy val root = gatlingProject(appName, ".")

  def gatlingProject(projectId: String, folder: String) = {
    Project(id = projectId, base = file(folder))
      .enablePlugins(GatlingPlugin,CorePlugin, JvmPlugin, IvyPlugin)
      .settings(projectSettings)
      .settings(`show-classpath`)
      .settings(scalacOptions ++= scalaOptions)
      .settings(libraryDependencies ++= Seq(
        Dependencies.Compile.nscalaTime,
        Dependencies.Compile.typesafeConfig,
        Dependencies.Compile.gatlingHighCharts,
        Dependencies.Compile.gatlingTestFramework,
        Dependencies.Compile.performanceTestRunner,
        Dependencies.Compile.gatlingVTDXMLPlugin))
      .settings(
        retrieveManaged := true,
        initialCommands in console := "import uk.gov.hmrc._",
        parallelExecution in Test := false,
        resolvers := Seq(Resolver.bintrayRepo("hmrc", "releases"))
      )
  }

  lazy val `show-classpath` = taskKey[Unit]("show-classpath") <<= (fullClasspath in Runtime) map { cp =>
    println(cp.files.absString)
  }
}