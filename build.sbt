addCommandAlias("build", ";headerCreate;compile;test")
addCommandAlias("rebuild", ";clean;build")

sonatypeProfileName := "com.github.vagmcs"

lazy val intervalTree = Project("IntervalTree", file("."))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(logLevel in Test := Level.Info)
  .settings(logLevel in Compile := Level.Error)
  .settings(libraryDependencies ++= Dependencies.ScalaTest)