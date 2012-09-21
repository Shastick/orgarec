package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedString

class SectionMap extends LongKeyedMapper[SectionMap] with IdPK {
	def getSingleton = SectionMap
	
	def name_len = 200
	
	object name extends MappedString(this, name_len)
}

object SectionMap extends SectionMap with LongKeyedMetaMapper[SectionMap]