package ch.epfl.craft.recom.processing.maps
import ch.epfl.craft.recom.storage.maps.TopicMap
import net.liftweb.mapper.Mapper
import net.liftweb.mapper.MetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedDouble
import net.liftweb.mapper.MappedString

/**
 * 'Meta' relation class to hold any relation fitting in one value between two topics.
 */
class TopicRelationMap extends Mapper[TopicRelationMap]{
	def getSingleton = TopicRelationMap
	
	val cls_name_len = 64
	
	object from extends MappedLongForeignKey(this, TopicMap){
	  override def dbIndexed_? = true
	}
	object to extends MappedLongForeignKey(this, TopicMap){
	  override def dbIndexed_? = true
	}
	
	object value extends MappedDouble(this){
	  override def dbIndexed_? = true
	}
	object relation extends MappedString(this, cls_name_len){
	  override def dbIndexed_? = true
	}
}

object TopicRelationMap extends TopicRelationMap with MetaMapper[TopicRelationMap]{
}