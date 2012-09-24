package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.HasManyThrough
import net.liftweb.mapper.MappedString
import ch.epfl.craft.recom.storage.assist.Prerequisite

class TopicMap extends LongKeyedMapper[TopicMap] with IdPK {
	def getSingleton = TopicMap
	
	val name_len = 200
	val isa_id_len = 20
	val descr_len = 10000
	
	object isa_id extends MappedString(this, isa_id_len)
	object name extends MappedString(this, name_len)
	object section extends MappedLongForeignKey(this,SectionMap)
	object description extends MappedString(this, descr_len)

	// Prerequisites
	object prerequisites extends HasManyThrough(this, TopicMap, Prerequisite,
	    Prerequisite.required,Prerequisite.course)
}

object TopicMap extends TopicMap with LongKeyedMetaMapper[TopicMap] {
  	
}