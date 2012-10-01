package ch.epfl.craft.recom.dimport.isa
import scala.io.Source
import scala.xml.NodeSeq
import scala.xml.Unparsed
import scala.xml.XML
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.administration.Teacher
import ch.epf.craft.recom.

object ISAxmlImporter extends App {
	
  val courses_url = "http://isa.epfl.ch/wsa/cles/ClesInscr?invoke=getlistecles&wwXAnneeacad=2012-2013"
  val xml = XML.loadString(Source.fromURL(courses_url).mkString)
  val items = xml \\ "item" 
  var ss = new Array[Set[Array[String]]](5)
  ss = Array(Set.empty,Set.empty,Set.empty,Set.empty,Set.empty)
  
  items.foreach{ i => 
    val f = (i \ "XCle").text.split("_").toList
  	val lib = (i \ "XLibelle").text.split(";").toIterator

  	f.filter(_.substring(0,1) == "M").map{ s =>
  	  new Topic(s, lib.next, Section(""), Set.empty, None)
    }
    
  	f.filter(_.substring(0,1) == "G").foreach(s => lib.next())
  	
  	f.filter(_.substring(0,1) == "P").foreach()
  	
  	/*
  	printf("%d elements: ",lib.size)

  	lib.size match {
  	  case 1 => ss(0) = ss(0) + lib
  	    lib.foreach(printf("%s | ",_))
  	  case 2 => ss(1) = ss(1) + lib
  	    lib.foreach(printf("%s | ",_))
  	  case 3 => ss(2) = ss(2) + lib
  		val name = lib(0)
  		val loc = lib(1).split(",")(0)
  		val prof = lib(2)
  		
  		printf("%s | %s | %s ", name, loc, prof)
  	  case 4 => ss(3) = ss(3) + lib
  	  	lib.foreach(printf("%s | ",_))
  	  case _ =>  ss(4) = ss(4) + lib
  	    lib.foreach(printf("%s | ",_))

  	}
  	print("\n") */
  }
    	ss.foreach(s => printf("%d\n",s.size))

  
  
  def makeCourses(il: NodeSeq) = il.map{
    null
  }
	
}