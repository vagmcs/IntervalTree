addCommandAlias("build", ";headerCreate;compile;test")
addCommandAlias("rebuild", ";clean;build")

useGpg := true

sonatypeProfileName := "com.github.vagmcs"

lazy val scalaTIKZ = Project("IntervalTree", file("."))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(logLevel in Test := Level.Info)
  .settings(logLevel in Compile := Level.Error)
  .settings(libraryDependencies ++= Dependency.ScalaTest)