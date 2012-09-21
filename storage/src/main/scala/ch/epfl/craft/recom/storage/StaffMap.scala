package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.MappedLongForeignKey

/**
 * Regroups both teachers and assistants for simplicity's sake.
 */
class StaffMap extends LongKeyedMapper[StaffMap] with IdPK {
	def getSingleton = StaffMap
	
	val name_len = 100
	val title_len = 100
	
	object name extends MappedString(this, name_len)
	object section extends MappedLongForeignKey(this, SectionMap)
	// What this staff's role 'normally' is (assistant, teacher,...)
	object title extends MappedString(this,title_len)
	
}

object StaffMap extends StaffMap with LongKeyedMetaMapper[StaffMap]