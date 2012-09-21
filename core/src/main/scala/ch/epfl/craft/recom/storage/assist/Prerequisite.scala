package ch.epfl.craft.recom.storage.assist
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.CourseMap

/**
 * Mapper class representing the prerequisites relations between courses.
 */
class Prerequisite extends LongKeyedMapper[Prerequisite] with IdPK {
	def getSingleton = Prerequisite
	
	object course extends MappedLongForeignKey(this,CourseMap)
	object required extends MappedLongForeignKey(this,CourseMap)
}

object Prerequisite extends Prerequisite with LongKeyedMetaMapper[Prerequisite]