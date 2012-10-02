package ch.epfl.craft.recom.processing.maps
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedInt
import ch.epfl.craft.recom.storage.maps.TopicMap

class RelativeImportance extends LongKeyedMapper[RelativeImportance] with IdPK {
	def getSingleton = RelativeImportance
	
	object from extends MappedLongForeignKey(this, TopicMap)
	object to extends MappedLongForeignKey(this, TopicMap)
	
	object weight extends MappedInt(this)
}

object RelativeImportance extends RelativeImportance with LongKeyedMetaMapper[RelativeImportance]{
  
  
}