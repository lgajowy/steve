ThisBuild / scalaVersion := "3.1.0-RC2"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / githubWorkflowPublishTargetBranches := Seq()

val commonSettings = Seq(
  scalacOptions -= "-XfatalWarnings",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.2.9",
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.5" % Test,

    // "org.typelevel" %% "cats-mtl" % "1.2.1"
  ),
)

val shared = project.settings(
  commonSettings,
  libraryDependencies ++= Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.19.0-M10",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "0.19.0-M10"
  ),
)

val server = project.settings(commonSettings).dependsOn(shared)
val client = project.settings(commonSettings).dependsOn(shared)

val root = project
  .in(file("."))
  .settings(publish := {}, publish / skip := true)
  .aggregate(server, client, shared)
