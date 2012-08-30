import sbt._
import sbt.Defaults._ 
import com.github.siasia._
import WebPlugin.webSettings
import sbtassembly.Plugin._
import AssemblyKeys._
import Keys._
import com.untyped.sbtrunmode.Plugin._

object FlizitBuild extends Build {

  scalaVersion := "2.9.0-1"

  lazy val liftVersion = "2.4"
 } 
