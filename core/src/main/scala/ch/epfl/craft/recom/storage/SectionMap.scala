package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.By
import ch.epfl.craft.recom.model.administration.Section

class SectionMap extends LongKeyedMapper[SectionMap] with IdPK {
	def getSingleton = SectionMap
	
	def name_len = 200
	
	object name extends MappedString(this, name_len)
	
	def read: Section = SectionMap.fill(this)
}

object SectionMap extends SectionMap with LongKeyedMetaMapper[SectionMap]{
  
  def fill(sl: TraversableOnce[Section]): TraversableOnce[SectionMap] = 
    sl.map(fill _)
  
  def fill(s: Section): SectionMap = {
    val m = SectionMap.findAll(By(SectionMap.name, s.name))
    		.headOption.getOrElse(SectionMap.create.name(s.name))
    m.save
    m
  }
  
  def fill(s: SectionMap): Section = Section(s.name)
}