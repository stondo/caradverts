name := """scout24-tech-challenge"""
organization := "com.stondo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies ++= Seq(evolutions)
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.1"
//libraryDependencies += "org.scanamo" % "scanamo_2.12" % "1.0.0-M9"
//libraryDependencies += "org.scanamo" % "scanamo-testkit_2.12" % "1.0.0-M9"
//libraryDependencies += "org.scanamo" % "scanamo-alpakka_2.12" % "1.0.0-M9"
//libraryDependencies += "org.scanamo" % "scanamo-refined_2.12" % "1.0.0-M9"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.1"
libraryDependencies += "com.github.tminglei" % "slick-pg_2.12" % "0.17.2"
libraryDependencies += "com.github.tminglei" %% "slick-pg_play-json" % "0.17.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.1"
libraryDependencies += "be.venneborg" % "play27-refined_2.12" % "0.4.0"
libraryDependencies += "eu.timepit" % "refined_2.12" % "0.9.5"
libraryDependencies += "de.swsnr" %% "play-json-refined" % "0.6.1"
libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "5.2"
libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.16"
//libraryDependencies += "org.mockito" % "mockito-scala_2.12" % "1.4.0-beta.7"
libraryDependencies += "com.h2database" % "h2" % "1.4.199" % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.stondo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.stondo.binders._"
