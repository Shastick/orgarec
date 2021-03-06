package ch.epfl.craft.recom.storage.maps
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

/**
 * Defines how a Semester is stored in the database
 */
class SemesterMap extends LongKeyedMapper[SemesterMap] with IdPK {
	def getSingleton = SemesterMap
	
	val season_len = 10
	
	object year extends MappedDate(this){
	  override def dbIndexed_? = true
	}
	object semester extends MappedString(this,season_len){
	  override def dbIndexed_? = true
	}
	
	def read = SemesterMap.fill(this)
}

object SemesterMap extends SemesterMap with LongKeyedMetaMapper[SemesterMap] {
  
  def fill(sl: Iterable[Semester]): Iterable[SemesterMap] = 
    sl.map(s => fill(s))
    
  def fill(s: Semester): SemesterMap = {
    val m = SemesterMap.findOrCreate(By(SemesterMap.year,s.year.toDate),By(SemesterMap.semester, s.season))
    m.year(s.year.toDate)
    m.semester(s.season)
    m.save
    m
  }
  
  def fill(s: SemesterMap):Semester = Semester(s.year.get,s.semester)
  
  def readMap(s: Semester): Option[SemesterMap] = 
    SemesterMap.findAll(By(SemesterMap.year, s.year.toDate), By(SemesterMap.semester, s.season)).headOption
  
}