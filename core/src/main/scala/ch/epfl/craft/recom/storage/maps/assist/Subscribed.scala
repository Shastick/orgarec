package ch.epfl.craft.recom.storage.maps.assist
import ch.epfl.craft.recom.storage.maps.CourseMap
import ch.epfl.craft.recom.storage.maps.StudentMap
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedInt
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.model.TakenCourse
import ch.epfl.craft.recom.model.Student
import net.liftweb.mapper.By
import ch.epfl.craft.recom.storage.maps.AcademicSemesterMap

/**
 * Mapper class representing the relation between a student and the courses he takes.
 */
class Subscribed extends LongKeyedMapper[Subscribed] with IdPK {
	def getSingleton = Subscribed
	
	object student extends MappedLongForeignKey(this, StudentMap){
	  override def dbIndexed_? = true
	}
	object course extends MappedLongForeignKey(this, CourseMap){
	  override def dbIndexed_? = true
	}
	object academicSemester extends MappedLongForeignKey(this, AcademicSemesterMap){
	  override def dbIndexed_? = true
	}
	
	object counter extends MappedInt(this){
	  override def dbIndexed_? = true
	}
	object grade extends MappedInt(this){
	  override def dbIndexed_? = true
	}
	object evaluation extends MappedInt(this){
	  override def dbIndexed_? = true
	}
}

object Subscribed extends Subscribed with LongKeyedMetaMapper[Subscribed]{
  
	def subscribe(sm: StudentMap, t: TakenCourse) = {
	  
	  val cm = CourseMap.fill(t.course)
	  val sub = Subscribed.findOrCreate(
			  			By(Subscribed.course,cm),
	  	      			By(Subscribed.student,sm))
	  sub.academicSemester(AcademicSemesterMap.fill(t.semester))
	  sub.student(sm)
	  sub.course(cm)
	  sub.counter(t.count)
	  t.grade.foreach(sub.grade(_))
	  t.evaluation.foreach(sub.evaluation(_))
	  sub.save
	  sub
	}
	
}