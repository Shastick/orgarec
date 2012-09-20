package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.MappedInt
import net.liftweb.mapper.MappedLongForeignKey

class StudentMap extends LongKeyedMapper[StudentMap] with IdPK{
	def getSingleton = StudentMap
	
	object scipper extends MappedInt(this)
	object arrival 
	object section extends MappedLongForeignKey(this,SectionMap)
	object currentSemester extends MappedLongForeignKey(this,SemesterMap)
	
	// semesterHistory
	// TODO
	// courses
	// TODO
	
}

object StudentMap extends StudentMap with LongKeyedMetaMapper[StudentMap]