package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.MappedDate

class SemesterMap extends LongKeyedMapper[SemesterMap] with IdPK {
	def getSingleton = SemesterMap
	
	val season_len = 10
	
	object year extends MappedDate(this)
	object semester extends MappedString(this,season_len)
}

object SemesterMap extends SemesterMap with LongKeyedMetaMapper[SemesterMap]