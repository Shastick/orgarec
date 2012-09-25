package ch.epfl.craft.recom.storage
import ch.epfl.craft.recom.model.administration.Fall
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Spring
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.mapper.By
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.MappedDate

class SemesterMap extends LongKeyedMapper[SemesterMap] with IdPK {
	def getSingleton = SemesterMap
	
	val season_len = 10
	
	object year extends MappedDate(this)
	object semester extends MappedString(this,season_len)
	
	def read = SemesterMap.fill(this)
}

object SemesterMap extends SemesterMap with LongKeyedMetaMapper[SemesterMap] {
  
  def fill(sl: TraversableOnce[Semester]): TraversableOnce[SemesterMap] = 
    sl.map(fill _)
    
  def fill(s: Semester): SemesterMap = {
    val season = s match {
      case Spring(_) => "spring"
      case Fall(_) => "fall"
    }
    val m = SemesterMap.findAll(By(SemesterMap.year,s.year),By(SemesterMap.semester, season))
    .headOption.getOrElse(SemesterMap.create.year(s.year).semester(season))
    m.save
    m
  }
  
  def fill(s: SemesterMap):Semester = Semester(s.year,s.semester)
}