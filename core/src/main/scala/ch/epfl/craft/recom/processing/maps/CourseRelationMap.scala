package ch.epfl.craft.recom.processing.maps
import net.liftweb.mapper.MappedDouble
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.Mapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.IdPK
import ch.epfl.craft.recom.storage.maps.CourseMap
import net.liftweb.mapper.MetaMapper


/**
 * 'Meta' relation class to store (in the database) any relation fitting in one value between two topics.
 */
class CourseRelationMap extends Mapper[CourseRelationMap] {
	def getSingleton = CourseRelationMap
	
	val cls_name_len = 64
	
	object from extends MappedLongForeignKey(this, CourseMap){
	  override def dbIndexed_? = true
	}
	object to extends MappedLongForeignKey(this, CourseMap){
	  override def dbIndexed_? = true
	}
	
	object value extends MappedDouble(this){
	  override def dbIndexed_? = true
	}
	object relation extends MappedString(this, cls_name_len){
	  override def dbIndexed_? = true
	}
}

object CourseRelationMap extends CourseRelationMap with MetaMapper[CourseRelationMap]{
  
}