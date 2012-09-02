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

  lazy val liftProjectSettings = webSettings ++ Seq(
    libraryDependencies ++= Seq(
      "org.mortbay.jetty" % "jetty" % "6.1.22" % "container",
      "net.liftweb" %% "lift-webkit" % liftVersion % "compile"
    )) ++ runModeSettings

  lazy val storage_settings = Seq(
  	libraryDependencies ++= Seq(
	  "postgresql" % "postgresql" % "9.1-901.jdbc4"
  ))

  lazy val root = Project(
  	id = "OrgaRec",
	base = file(".")
  ) aggregate(input,model,storage,view)

  lazy val input = Project(
   	id = "input",
	base = file("input")
  ) dependsOn(model)

  lazy val model = Project(
  	id = "model",
	base = file("model")
  ) dependsOn(storage)

  lazy val storage = Project(
  	id = "storage",
	base = file("storage")
  ) settings(storage_settings : _*) 

  lazy val view = Project(
  	id = "view",
	base = file("view")
  ) settings(liftProjectSettings : _*) dependsOn(model)
} 

