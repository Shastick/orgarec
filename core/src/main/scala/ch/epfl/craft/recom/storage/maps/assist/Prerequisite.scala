package ch.epfl.craft.recom.storage.maps.assist
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.maps.CourseMap
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.storage.maps.TopicMap
import ch.epfl.craft.recom.model.Topic
import net.liftweb.mapper.By

/**
 * Mapper class representing the prerequisites relations between courses.
 */
class Prerequisite extends LongKeyedMapper[Prerequisite] with IdPK {
	def getSingleton = Prerequisite
	
	object topic extends MappedLongForeignKey(this,TopicMap)
	object required extends MappedLongForeignKey(this,TopicMap)
}

object Prerequisite extends Prerequisite with LongKeyedMetaMapper[Prerequisite] {
  
  def setPrerequisites(t: Topic, tm: TopicMap) = 
    t.prerequisites.map{ rt => 
    	val rtm = TopicMap.find(By(TopicMap.isa_id, rt.id))
    				.getOrElse(throw new Exception("Prerequisite not existing in DB. " +
    						"Have you imported every Topic before setting prerequisites ?"))
    	
    	val pm = Prerequisite.findOrCreate(By(Prerequisite.topic,tm),By(Prerequisite.required,rtm))
    	pm.topic(tm)
    	  .required(rtm)

    	pm.save()
    	pm
  }
    
  
}