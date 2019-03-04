
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._

object Build extends AutoPlugin {

  private val logger = ConsoleLogger()

  override def requires: Plugins = JvmPlugin && HeaderPlugin

  // Allow the plug-in to be included automatically
  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = settings

  private val javaVersion: Double = sys.props("java.specification.version").toDouble

  private lazy val settings: Seq[Setting[_]] = {
    logger.info(s"Loading settings for Java $javaVersion or higher.")
    if (javaVersion < 1.8) sys.error("Java 8 or higher is required for building Optimus.")
    else commonSettings ++ JavaSettings ++ ScalaSettings ++ Reform.formatSettings
  }

  private val commonSettings: Seq[Setting[_]] = Seq(

    name := "IntervalTree",

    organization := "com.github.vagmcs",

    description := "Interval tree",

    headerLicense := Some(HeaderLicense.LGPLv3("2018", "Evangelos Michelioudakis")),

    headerMappings := headerMappings.value + (HeaderFileType.scala -> HeaderCommentStyle.cStyleBlockComment),

    scalaVersion := "2.12.8",

    crossScalaVersions := Seq("2.12.8", "2.11.12"),

    autoScalaLibrary := true,

    managedScalaInstance := true,

    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },

    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.typesafeRepo("releases"),
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    ),

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

    scmInfo := Some(
      ScmInfo(url("https://github.com/vagmcs/IntervalTree"), "scm:git:git@github.com:vagmcs/IntervalTree.git")
    ),

    // Information required in order to sync in Maven Central
    pomExtra :=
      <url>https://github.com/vagmcs</url>
        <licenses>
          <license>
            <name>GNU Lesser General Public License v3.0</name>
            <url>https://www.gnu.org/licenses/lgpl-3.0.en.html</url>
          </license>
        </licenses>
        <developers>
          <developer>
            <id>vagmcs</id>
            <name>Evangelos Michelioudakis</name>
            <url>https://users.iit.demokritos.gr/~vagmcs/</url>
          </developer>
        </developers>
  )

  private lazy val JavaSettings: Seq[Setting[_]] = Seq(

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    
    javaOptions ++= Seq(
      "-XX:+DoEscapeAnalysis",
      "-XX:+UseFastAccessorMethods",
      "-XX:+OptimizeStringConcat")
  )

  private lazy val ScalaSettings: Seq[Setting[_]] = Seq(
    scalacOptions := {
      scalaBinaryVersion.value match {

        case "2.11" =>
          // Scala compiler settings for Scala 2.11.x
          Seq(
            "-deprecation",       // Emit warning and location for usages of deprecated APIs.
            "-unchecked",         // Enable additional warnings where generated code depends on assumptions.
            "-feature",           // Emit warning and location for usages of features that should be imported explicitly.
            "-target:jvm-1.8",    // Target JVM version 1.8.
            "-Yinline-warnings",  // Emit inlining warnings
            "-Yclosure-elim",     // Perform closure elimination
            "-Ybackend:GenBCode"  // Use the new optimisation level
          )

        case "2.12" =>
          // Scala compiler settings for Scala 2.12.x
          Seq(
            "-deprecation",       // Emit warning and location for usages of deprecated APIs.
            "-unchecked",         // Enable additional warnings where generated code depends on assumptions.
            "-feature",           // Emit warning and location for usages of features that should be imported explicitly.
            "-target:jvm-1.8",    // Target JVM version 1.8.
          )

        case _ => 
          logger.error(s"Unsupported version of Scala '${scalaBinaryVersion.value}'")
          sys.exit(1)
      }
    }
  )
}
