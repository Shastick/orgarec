package ch.epfl.craft.recom.storage.assist
import ch.epfl.craft.recom.storage.CourseMap
import ch.epfl.craft.recom.storage.StudentMap
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedInt
import net.liftweb.mapper.MappedLongForeignKey
import ch.epfl.craft.recom.model.TakenCourse
import ch.epfl.craft.recom.model.Student
import net.liftweb.mapper.By

/**
 * Mapper class representing the relation between a student and the courses he takes.
 */
class Subscribed extends LongKeyedMapper[Subscribed] with IdPK {
	def getSingleton = Subscribed
	
	object student extends MappedLongForeignKey(this, StudentMap)
	object course extends MappedLongForeignKey(this, CourseMap)
	
	object counter extends MappedInt(this)
	object grade extends MappedInt(this)
	object evaluation extends MappedInt(this)
}

object Subscribed extends Subscribed with LongKeyedMetaMapper[Subscribed]{
  
	def subscribe(sm: StudentMap, t: TakenCourse) = {
	  
	  val cm = CourseMap.fill(t.course)
	  val sub = Subscribed.findOrCreate(
			  			By(Subscribed.course,cm.id),
	  	      			By(Subscribed.student,sm.id))
	  sub.student(sm.id)
	  sub.course(cm.id)
	  sub.counter(t.count)
	  t.grade.foreach(sub.grade(_))
	  t.evaluation.foreach(sub.evaluation(_))
	  sub.save
	  sub
	}
	
}