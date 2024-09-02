

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"


lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    ScoverageKeys.coverageEnabled := true,
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageOutputHTML := true
  )
}


lazy val root = (project in file("."))
  .settings(
    name := "google book api"
  )
  .enablePlugins(PlayScala)

resolvers += "HMRC-open-artefacts-maven2" at "https://open.artefacts.tax.service.gov.uk/maven2"

libraryDependencies ++= Seq(
  "uk.gov.hmrc.mongo"      %% "hmrc-mongo-play-28"      % "0.63.0",             guice,
  "org.scalatest"          %% "scalatest"               % "3.2.15"            % Test,
  "org.scalamock"          %% "scalamock"               % "5.2.0"             % Test,
  "org.scalatestplus.play" %% "scalatestplus-play"      % "5.1.0"             % Test

)



libraryDependencies += ws
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.2"
libraryDependencies += ("org.typelevel"                %% "cats-core"                 % "2.3.0")


