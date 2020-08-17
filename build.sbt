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
  name := "upscan-performance-tests",
  organization := "uk.gov.hmrc",
  version := "999-SNAPSHOT",
  scalaVersion := "2.12.12"
)

lazy val root = (project in file("."))
  .enablePlugins(GatlingPlugin, CorePlugin, JvmPlugin, IvyPlugin)
  .settings(projectSettings)
  .settings(showClasspath)
  .settings(scalacOptions ++= scalaOptions)
  .settings(libraryDependencies ++= Seq(
    Dependencies.Compile.nscalaTime,
    Dependencies.Compile.typesafeConfig,
    Dependencies.Compile.gatlingHighCharts,
    Dependencies.Compile.gatlingTestFramework,
    Dependencies.Compile.performanceTestRunner,
    Dependencies.Compile.json4sJackson,
    Dependencies.Compile.json4sNative,
    Dependencies.Compile.gatlingVTDXMLPlugin))
  .settings(
    retrieveManaged := true,
    initialCommands in console := "import uk.gov.hmrc._",
    parallelExecution in Test := false,
    resolvers := Seq(Resolver.bintrayRepo("hmrc", "releases"))
  )


lazy val showClasspath = taskKey[Unit]("show-classpath") := println((fullClasspath in Runtime).value.files.absString)
