package ch.epfl.craft.recom.dimport.isa
import scala.io.Source
import scala.xml.NodeSeq
import scala.xml.Unparsed
import scala.xml.XML
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.administration.Teacher
import ch.epfl.craft.recom.model.administration.Head
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.Course

object ISAxmlImporter extends App {
	
  val courses_url = "http://isa.epfl.ch/wsa/cles/ClesInscr?invoke=getlistecles&wwXAnneeacad=2012-2013"
  val xml = XML.loadString(Source.fromURL(courses_url).mkString)
  val items = xml \\ "item" 
  var ss = new Array[Set[Array[String]]](5)
  ss = Array(Set.empty,Set.empty,Set.empty,Set.empty,Set.empty)
  
  val sem = Semester("2012", "fall")
  
  val cl = items.map{ i => 
    val f = (i \ "XCle").text.split("_").toList
  	val lib = (i \ "XLibelle").text.split(";").toIterator

  	val topic = f.find(s =>  s.size > 1 && s.substring(0,1) == "M").map(s =>
  	  new Topic(s, lib.next, Section(""), Set.empty, None))
    
  	f.filter(s => s.size > 1 && s.substring(0,1) == "G").foreach(s => lib.next())
  	
  	val teachers = f.filter(s => s.size > 1 && s.substring(0,1) == "P").map(s =>
  	  new Teacher(s, None)).toList
  	
  	val head = Head(teachers, List.empty)
  	
  	topic.map(Course(_, sem, head))
  }.flatten
	cl.foreach(println _)
}