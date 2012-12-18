package ch.epfl.craft.recom.storage.maps
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.By
import ch.epfl.craft.recom.model.administration.Staff
import ch.epfl.craft.recom.model.administration.Teacher
import ch.epfl.craft.recom.model.administration.Assistant

/**
 * Defines how a Staff is stored.
 * 
 * Regroups both teachers and assistants for simplicity's sake.
 */
class StaffMap extends LongKeyedMapper[StaffMap] with IdPK {
	def getSingleton = StaffMap
	
	val name_len = 100
	val title_len = 100
	
	object name extends MappedString(this, name_len){
	  override def dbIndexed_? = true
	}
	object section extends MappedLongForeignKey(this, SectionMap){
	  override def dbIndexed_? = true
	}
	// What this staff's role 'normally' is (assistant, teacher,...)
	object title extends MappedString(this,title_len){
	  override def dbIndexed_? = true
	}
	
	def read = StaffMap.fill(this)
	
}

object StaffMap extends StaffMap with LongKeyedMetaMapper[StaffMap] {
  
  def fill(sl: Iterable[Staff]): Iterable[StaffMap] =
    sl.map(fill _)
  
  def fill(s: Staff): StaffMap = {
    val m = StaffMap.findOrCreate(By(StaffMap.name,s.name))
    m.name(s.name)
    s.section.foreach(sec => m.section(SectionMap.fill(sec)))
    m.title(s match {case Teacher(_,_) => "teacher" ; case Assistant(_,_) => "assistant"})
    m.save
    m
  }
  
  def fill(s: StaffMap): Staff = 
    s.title.get match {
      case "teacher" => Teacher(s.name,s.section.map(_.read))
      case "assistant" => Assistant(s.name, s.section.map(_.read))
    }
  
  def read(name: String): Option[Staff] = 
    StaffMap.findAll(By(StaffMap.name, name)).headOption.map(fill(_))
  
}