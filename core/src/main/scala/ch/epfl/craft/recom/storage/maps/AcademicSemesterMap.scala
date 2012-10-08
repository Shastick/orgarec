package ch.epfl.craft.recom.storage.maps
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedString
import ch.epfl.craft.recom.model.administration.AcademicSemester
import net.liftweb.mapper.By

class AcademicSemesterMap extends LongKeyedMapper[AcademicSemesterMap] with IdPK {
	def getSingleton = AcademicSemesterMap
	
	val lvl_str_len = 5
	
	object semester extends MappedLongForeignKey(this, SemesterMap)
	object level extends MappedString(this, lvl_str_len)
	
	def read = AcademicSemesterMap.fill(this)
	
}

object AcademicSemesterMap extends AcademicSemesterMap with LongKeyedMetaMapper[AcademicSemesterMap]{
  
  def fill(in: AcademicSemester): AcademicSemesterMap = {
    val s = SemesterMap.fill(in.semester)
    val c = in.getClass.getSimpleName
    val am = AcademicSemesterMap.findOrCreate(
        By(this.semester, s),
        By(this.level, c))
   am.semester(s)
   am.level(c)
   am.save
   am
  }
  
  def fill(m: AcademicSemesterMap): AcademicSemester = 
   AcademicSemester(m.level.get, m.semester.map(_.read).get)
  
}