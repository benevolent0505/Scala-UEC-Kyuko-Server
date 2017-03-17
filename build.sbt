name := """Scala-UEC-Kyuko-Server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  evolutions,
  "org.postgresql"  %  "postgresql"                     % "42.0.0",
  "org.scalikejdbc" %% "scalikejdbc"                    % "2.5.1",
  "org.scalikejdbc" %% "scalikejdbc-config"             % "2.5.1",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer"   % "2.5.1",
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.5.1",

  "joda-time" % "joda-time" % "2.9.7",
  "net.ruippeixotog" %% "scala-scraper" % "1.2.0",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",

  "ch.qos.logback" % "logback-classic" % "1.2.1",

  "org.scalactic"          %% "scalactic"          % "3.0.1",
  "org.scalatest"          %% "scalatest"          % "3.0.1" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)
