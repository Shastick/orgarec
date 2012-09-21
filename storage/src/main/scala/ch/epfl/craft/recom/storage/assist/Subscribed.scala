package ch.epfl.craft.recom.storage.assist
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.StudentMap
import ch.epfl.craft.recom.storage.CourseMap

/**
 * Mapper class representing the relation between a student and the courses he takes.
 */
class Subscribed extends LongKeyedMapper[Subscribed] with IdPK {
	def getSingleton = Subscribed
	
	object student extends MappedLongForeignKey(this, StudentMap)
	object course extends MappedLongForeignKey(this, CourseMap)
}

object Subscribed extends Subscribed with LongKeyedMetaMapper[Subscribed]{

}