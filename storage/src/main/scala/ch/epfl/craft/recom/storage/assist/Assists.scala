package ch.epfl.craft.recom.storage.assist
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.StaffMap
import ch.epfl.craft.recom.storage.CourseMap

/**
 * Relation between a course and the staff (teacher or assistant...) that assists it.
 */
class Assists extends LongKeyedMapper[Assists] with IdPK{
	def getSingleton = Assists
	
	object assistant extends MappedLongForeignKey(this, StaffMap)
	object course extends MappedLongForeignKey(this, CourseMap)
}

object Assists extends Assists with LongKeyedMetaMapper[Assists]