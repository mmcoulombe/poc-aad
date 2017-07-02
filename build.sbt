name := "async-action-driven"

version := "1.0"

resolvers ++= Seq(
  DefaultMavenRepository,
  Resolver.bintrayRepo("hseeberger", "maven")
)

lazy val scalaV = "2.12.1"

lazy val server = (project in file("server"))
  .settings(
    scalaVersion := scalaV,
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.9",
      "de.heikoseeberger" %% "akka-http-play-json" % "1.17.0",
      "com.vmunier" %% "scalajs-scripts" % "1.1.0"
    ),
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value
  ).enablePlugins(SbtWeb, SbtTwirl)
   .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .settings(
    scalaVersion := scalaV,
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "io.suzaku" %%% "diode" % "1.1.2",
      "io.suzaku" %%% "diode-react" % "1.1.2",
      "com.github.japgolly.scalajs-react" %%% "core" % "1.0.1"
    ),
    jsDependencies ++= Seq(
      "org.webjars.bower" % "react" % "15.5.4"
        /        "react-with-addons.js"
        minified "react-with-addons.min.js"
        commonJSName "React",

      "org.webjars.bower" % "react" % "15.5.4"
        /         "react-dom.js"
        minified  "react-dom.min.js"
        dependsOn "react-with-addons.js"
        commonJSName "ReactDOM",

      "org.webjars.bower" % "react" % "15.5.4"
        /         "react-dom-server.js"
        minified  "react-dom-server.min.js"
        dependsOn "react-dom.js"
        commonJSName "ReactDOMServer"
    )
  ).enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)

lazy val shared =
  (crossProject.crossType(CrossType.Pure) in file ("shared"))
    .settings(
      scalaVersion := scalaV,
      libraryDependencies += "com.typesafe.play" %%% "play-json" % "2.6.0"
    )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value