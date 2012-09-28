package ch.epfl.craft.recom.storage.maps.assist
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.KeyedMetaMapper
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.maps.CourseMap
import ch.epfl.craft.recom.storage.maps.StaffMap
import ch.epfl.craft.recom.model.Course
import net.liftweb.mapper.By

/**
 * Mapper class representing the relation between courses and the staff 
 * that head (and teach) them.
 */
class Teaches extends LongKeyedMapper[Teaches] with IdPK {
	def getSingleton = Teaches
	
	object teacher extends MappedLongForeignKey(this, StaffMap)
	object course extends MappedLongForeignKey(this, CourseMap)
}

object Teaches extends Teaches with LongKeyedMetaMapper[Teaches] {
  
  def setTeachersFor(c: Course, cm: CourseMap) = {
    c.head.teachers.map{ t => 
    	val sm = StaffMap.fill(t)
    	val tm = Teaches.findOrCreate(
    		By(Teaches.course, cm),
    		By(Teaches.teacher, sm))
    	tm.course(cm).teacher(sm)
    	tm.save()
    	tm
    }
  }
}