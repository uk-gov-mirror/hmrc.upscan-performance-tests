logLevel := Level.Warn

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

val hmrcRepoHost = java.lang.System.getProperty("hmrc.repo.host", "https://nexus-dev.tax.service.gov.uk")

resolvers ++= Seq(
  "hmrc-snapshots" at hmrcRepoHost + "/content/repositories/hmrc-snapshots",
  "hmrc-releases" at hmrcRepoHost + "/content/repositories/hmrc-releases",
  Resolver.url("hmrc-sbt-plugin-releases",
    url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("uk.gov.hmrc" % "hmrc-resolvers" % "1.2.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "1.8.0")

addSbtPlugin("io.gatling" % "gatling-sbt" % "2.1.7")
