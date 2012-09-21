package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.MappedInt
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.HasManyThrough
import ch.epfl.craft.recom.storage.assist.Subscribed

class StudentMap extends LongKeyedMapper[StudentMap] with IdPK{
	def getSingleton = StudentMap
	
	object scipper extends MappedInt(this)
	object arrival 
	object section extends MappedLongForeignKey(this,SectionMap)
	object currentSemester extends MappedLongForeignKey(this,SemesterMap)
	
	// Passed and current subscriptions to courses
	// The semester history can be rebuilt from here,
	// so unless we have performance issues we won't do an extra relation for
	// that
	object subscriptions extends HasManyThrough(this, CourseMap, Subscribed,
	    Subscribed.course, Subscribed.student)
	
}

object StudentMap extends StudentMap with LongKeyedMetaMapper[StudentMap]