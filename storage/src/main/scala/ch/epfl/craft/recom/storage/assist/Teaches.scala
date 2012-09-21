package ch.epfl.craft.recom.storage.assist
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.KeyedMetaMapper
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.CourseMap
import ch.epfl.craft.recom.storage.StaffMap

/**
 * Mapper class representing the relation between courses and the staff 
 * that head (and teach) them.
 */
class Teaches extends LongKeyedMapper[Teaches] with IdPK {
	def getSingleton = Teaches
	
	object teacher extends MappedLongForeignKey(this, StaffMap)
	object course extends MappedLongForeignKey(this, CourseMap)
}

object Teaches extends Teaches with LongKeyedMetaMapper[Teaches] {
  
}