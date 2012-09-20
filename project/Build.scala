import sbt._
import sbt.Defaults._ 
import com.github.siasia._
import WebPlugin.webSettings
import sbtassembly.Plugin._
import AssemblyKeys._
import Keys._
import com.untyped.sbtrunmode.Plugin._

object OrgaRecBuild extends Build {

  scalaVersion := "2.9.0-1"

  lazy val liftVersion ="2.4"

  lazy val liftProjectSettings = Seq(libraryDependencies ++= Seq(
        "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
        "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
	"net.liftweb" %% "lift-wizard" % liftVersion % "compile->default", 
	"org.eclipse.jetty" % "jetty-webapp" % "8.0.4.v20111024" % "container", // For Jetty 8
	//"org.eclipse.jetty" % "jetty-webapp" % "7.3.0.v20110203" % "container", // For Jetty 7
	//"org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty,test", // For Jetty 6, add scope test to make jetty avl. for tests
  	"org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test", // For specs.org tests
  	"junit" % "junit" % "4.8" % "test->default", // For JUnit 4 testing
  	"javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  	"com.h2database" % "h2" % "1.2.138", // In-process database, useful for development systems
  	"ch.qos.logback" % "logback-classic" % "0.9.26" % "compile->default" // Logging
     ))++ webSettings ++ runModeSettings

  lazy val storage_settings = Seq(
  	libraryDependencies ++= Seq(
	  "postgresql" % "postgresql" % "9.1-901.jdbc4"
  ))

  lazy val root = Project(
  	id = "OrgaRec",
	base = file(".")
  ) aggregate(import_proj,middle,storage,view)

  lazy val import_proj = Project(
   	id = "import",
	base = file("import")
  ) dependsOn(middle)

  lazy val middle = Project(
  	id = "middle",
	base = file("middle")
  ) dependsOn(storage)

  lazy val storage = Project(
  	id = "storage",
	base = file("storage")
  ) settings(storage_settings : _*) 

  lazy val view = Project(
  	id = "view",
	base = file("view")
  ) settings(liftProjectSettings : _*) dependsOn(middle)
} 

