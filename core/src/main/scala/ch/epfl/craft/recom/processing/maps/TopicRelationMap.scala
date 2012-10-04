package ch.epfl.craft.recom.processing.maps
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedDouble
import ch.epfl.craft.recom.storage.maps.TopicMap
import net.liftweb.mapper.MappedString

/**
 * 'Meta' relation class to hold any relation fitting in one value between two topics.
 */
class TopicRelationMap extends LongKeyedMapper[TopicRelationMap] with IdPK{
	def getSingleton = TopicRelationMap
	
	val cls_name_len = 64
	
	object from extends MappedLongForeignKey(this, TopicMap)
	object to extends MappedLongForeignKey(this, TopicMap)
	
	object value extends MappedDouble(this)
	object relation extends MappedString(this, cls_name_len)
}

object TopicRelationMap extends TopicRelationMap with LongKeyedMetaMapper[TopicRelationMap]{
  
}