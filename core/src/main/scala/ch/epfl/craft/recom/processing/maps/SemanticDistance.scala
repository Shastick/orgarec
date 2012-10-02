package ch.epfl.craft.recom.processing.maps
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper

class SemanticDistance extends LongKeyedMapper[SemanticDistance] with IdPK {

}

object SemanticDisantce extends SemanticDistance with LongKeyedMetaMapper[SemanticDistance]