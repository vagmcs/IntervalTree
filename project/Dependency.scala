import sbt._

object Dependency {
  
  object v {
    val scalaTestVersion = "3.0.5"
    val scalaCheckVersion = "1.14.0"
  }
  
  // ScalaTest and ScalaCheck for UNIT testing
  lazy val ScalaTest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % v.scalaTestVersion % "test",
    "org.scalacheck" %% "scalacheck" % v.scalaCheckVersion % "test"
  )
}
