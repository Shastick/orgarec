package ch.epfl.craft.recom.dimport.isa
import scala.io.Source
import scala.xml.XML
import ch.epfl.craft.recom.storage.maps.TopicMap
import ch.epfl.craft.recom.storage.db.PGDBFactory
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.Topic
import scala.xml.NodeSeq

object TopicUpdate extends App {
    val dbf = new PGDBFactory("craftsrv5.epfl.ch","orgarec","julienp","dorloter89")
	val s = dbf.store
	
	println("loading xml...")
	val dat = XML.loadString(Source.fromFile("2012-2013-studyplan.xml").mkString)
	
	updateTopics(dat)
    
    def updateTopics(dat: NodeSeq) = {
    println("mapping xml...")
	val sps = dat \\ "study-plan"
	println(sps.size)
	sps.foreach{ sp => 
    	val sect = Section((sp \ "section").text)
		val courses = sp \\ "course"
		courses.foreach{ e => 
			  val isa_id = "M" + e \ "@id"
			  val code = (e \ "code").text
			  val name = (e \ "name").text
			  val lang = (e \ "language").text
				  
			  TopicMap.read(isa_id).foreach{ t => 
			    val n = new Topic(isa_id, name, sect, t.prerequisites_id, t.description, t.credits)
			    TopicMap.fill(n)
			  }
			}
    	println("Done a studyplan.")
    }
    }
}