val scalaOptions = Seq(
  "-feature"
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
  .settings(libraryDependencies ++= Dependencies.test)
  .settings(
    retrieveManaged := true,
    initialCommands in console := "import uk.gov.hmrc._",
    parallelExecution in Test := false,
    resolvers := Seq(Resolver.bintrayRepo("hmrc", "releases"))
  )


lazy val showClasspath = taskKey[Unit]("show-classpath") := println((fullClasspath in Test).value.files.absString)
