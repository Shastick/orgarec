package ch.epfl.craft.recom.storage.assist
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.CourseMap
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.storage.TopicMap
import ch.epfl.craft.recom.model.Topic

/**
 * Mapper class representing the prerequisites relations between courses.
 */
class Prerequisite extends LongKeyedMapper[Prerequisite] with IdPK {
	def getSingleton = Prerequisite
	
	object course extends MappedLongForeignKey(this,TopicMap)
	object required extends MappedLongForeignKey(this,TopicMap)
}

object Prerequisite extends Prerequisite with LongKeyedMetaMapper[Prerequisite] {
  
  def setPrerequisites(t: Topic, tm: TopicMap) = 
    c.prerequisites.map{ t => 
    	val pm = TopicMap.fill(t)
    	//TODO
  }
    
  
}