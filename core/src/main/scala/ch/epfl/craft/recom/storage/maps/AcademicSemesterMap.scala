package ch.epfl.craft.recom.storage.maps
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedString
import ch.epfl.craft.recom.model.administration.AcademicSemester

class AcademicSemesterMap extends LongKeyedMapper[AcademicSemesterMap] with IdPK {
	def getSingleton = AcademicSemesterMap
	
	val lvl_str_len = 5
	
	object semester extends MappedLongForeignKey(this, SemesterMap)
	object level extends MappedString(this, lvl_str_len)
	
}

object AcademicSemesterMap extends AcademicSemesterMap with LongKeyedMetaMapper[AcademicSemesterMap]{
  
  def fill(in: AcademicSemester): AcademicSemesterMap = throw new Exception
  
  def fill(m: AcademicSemesterMap): AcademicSemester = throw new Exception
  
}