package ch.epfl.craft.recom.storage.assist
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.storage.StaffMap
import ch.epfl.craft.recom.storage.CourseMap
import ch.epfl.craft.recom.model.Course
import net.liftweb.mapper.By

/**
 * Relation between a course and the staff (teacher or assistant...) that assists it.
 */
class Assists extends LongKeyedMapper[Assists] with IdPK{
	def getSingleton = Assists
	
	object assistant extends MappedLongForeignKey(this, StaffMap)
	object course extends MappedLongForeignKey(this, CourseMap)
}

object Assists extends Assists with LongKeyedMetaMapper[Assists] {
  
  def setAssistsFor(c: Course, cm: CourseMap) = {
    c.head.assistants.map{ a => 
      val sm = StaffMap.fill(a)
      val am = Assists.findOrCreate(By(Assists.course,cm),By(Assists.assistant,sm))
      am.course(cm).assistant(sm)
      am.save
      am
    }
  }
}