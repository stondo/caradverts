name := """scout24-tech-challenge"""
organization := "com.stondo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.1"
libraryDependencies += "org.scanamo" % "scanamo_2.12" % "1.0.0-M9"
libraryDependencies += "org.scanamo" % "scanamo-testkit_2.12" % "1.0.0-M9"
libraryDependencies += "org.scanamo" % "scanamo-alpakka_2.12" % "1.0.0-M9"
libraryDependencies += "org.scanamo" % "scanamo-refined_2.12" % "1.0.0-M9"
libraryDependencies += "eu.timepit" % "refined_2.12" % "0.9.5"
libraryDependencies += "de.swsnr" %% "play-json-refined" % "0.6.1"
libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "5.2"
libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.16"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.stondo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.stondo.binders._"
