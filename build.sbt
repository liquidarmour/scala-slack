
name := "scala-slack"

organization := "com.ponkotuy"

version := "0.5.0"

scalacOptions += "-target:jvm-1.6"

scalaVersion := "2.11.8"

// Publish settings

publishMavenStyle := true

publishTo := {
   val nexus = "https://oss.sonatype.org/"
   if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
   else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/ponkotuy/scala-slack"))

pomExtra := (
  <scm>
    <url>git@github.com:ponkotuy/scala-slack.git</url>
    <connection>git@github.com:ponkotuy/scala-slack.git</connection>
  </scm>
    <developers>
      <developer>
        <id>ponkotuy</id>
        <name>ponkotuy</name>
        <url>https://github.com/ponkotuy</url>
      </developer>
    </developers>)



libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.3.0",
  "org.scalaj" %% "scalaj-http" % "1.1.5",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
