lazy val commonSettings = Seq(
  scalaVersion := "2.11.8"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := """Scala-UEC-Kyuko-Server""",
    version := "1.0-SNAPSHOT",

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
      "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",

      "ch.qos.logback" % "logback-classic" % "1.2.1",

      "org.webjars"       % "bootstrap"          % "3.3.7",
      "org.webjars"       % "bootstrap-sass"     % "3.3.7",
      "org.webjars.bower" % "fullcalendar"       % "3.2.0",
      "org.webjars"       % "momentjs"           % "2.17.1",

      "org.scalactic"          %% "scalactic"          % "3.0.1",
      "org.scalatest"          %% "scalatest"          % "3.0.1" % "test",
      "org.scalikejdbc"        %% "scalikejdbc-test"   % "2.5.1" % "test",
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
    )
  ).enablePlugins(PlayScala)


lazy val devUtil = (project in file("devUtil")).
  settings(
    commonSettings
  )
  .dependsOn(root)

lazy val fetch = (project in file("fetch")).
  settings(
    commonSettings,
    name := "Kyuko-page-fetcher"
  )
  .dependsOn(root)
