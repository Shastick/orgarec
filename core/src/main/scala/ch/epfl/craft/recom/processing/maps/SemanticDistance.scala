package ch.epfl.craft.recom.processing.maps
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.maps.TopicMap
import net.liftweb.mapper.MappedDouble

class SemanticDistance extends LongKeyedMapper[SemanticDistance] with IdPK {
	def getSingleton = SemanticDistance
	
	object from extends MappedLongForeignKey(this, TopicMap)
	object to extends MappedLongForeignKey(this, TopicMap)
	
	object weight extends MappedDouble(this)
}

object SemanticDistance extends SemanticDistance with LongKeyedMetaMapper[SemanticDistance]