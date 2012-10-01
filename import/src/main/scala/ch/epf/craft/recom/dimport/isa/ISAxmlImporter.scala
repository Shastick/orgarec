package ch.epf.craft.recom.dimport.isa
import scala.io.Source
import scala.xml.NodeSeq
import scala.xml.Unparsed
import scala.xml.XML

object ISAxmlImporter extends App {
	
  val courses_url = "http://isa.epfl.ch/wsa/cles/ClesInscr?invoke=getlistecles&wwXAnneeacad=2012-2013"
  val xml = XML.loadString(Source.fromURL(courses_url).mkString)
  val items = xml \\ "item" 
  items.foreach{ i => 
  	val lib = (i \ "XLibelle").text.split(";")
  	if(lib.size > 2){
  		val name = lib(0)
  		val loc = lib(1).split(",")(0)
  		val prof = lib(2)
  		
  		println(name,loc,prof)

  	}

  }
  
  
  def makeCourses(il: NodeSeq) = il.map{
    null
  }
	
}