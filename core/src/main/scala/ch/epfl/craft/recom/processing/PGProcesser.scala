package ch.epfl.craft.recom.processing

import net.liftweb.db.ConnectionManager
import net.liftweb.db.ConnectionIdentifier
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.processing.maps.CourseRelationMap
import ch.epfl.craft.recom.storage.maps.CourseMap
import net.liftweb.mapper.By
import ch.epfl.craft.recom.util.TimeRange
import ch.epfl.craft.recom.model.administration.Section

class PGProcesser(ci: ConnectionIdentifier, db: ConnectionManager) extends Processer {
	
	def costudents_rel = "costudents"
  
	def computeCoStudents = {
	  
	}
	
	def readCoStudents(c1: Course, c2: Course) = {
	  val (cm1,cm2) = (CourseMap.fill(c1), CourseMap.fill(c2))
	  readCoStudents(cm1,cm2) match {
	    case Some(i) => i
	    case None => readCoStudents(cm2,cm1).getOrElse(0)
	  }
	}
	
	private def readCoStudents(cm1: CourseMap, cm2: CourseMap): Option[Int] = {
	  val rl = CourseRelationMap.findAll(
	      By(CourseRelationMap.from,cm1),
	      By(CourseRelationMap.to,cm2),
	      By(CourseRelationMap.relation,costudents_rel))
	  rl.size match {
	    case 0 => None
	    case 1 => Some(rl.head.value.toInt)
	    case 2 => throw new Exception("Double entries in the relation DB!") 
	    // TODO : log this instead of throwing lame exceptions...
	  }
	}
	
	def readCoStudents(c: Course, tr: TimeRange = TimeRange.all) = {
	  val cm = CourseMap.fill(c)
	  val l1 = CourseRelationMap.findAll(By(CourseRelationMap.from,cm),
			  					By(CourseRelationMap.relation,costudents_rel))
			  					.map(rm => (rm.to.map(_.read).get, rm.value.toInt))
						
	  val l2 = CourseRelationMap.findAll(By(CourseRelationMap.to,cm),
			  					By(CourseRelationMap.relation,costudents_rel))
			  					.map(rm => (rm.from.map(_.read).get, rm.value.toInt))
	  (l1 ++ l2).filter(t => t._1.semester >= tr.from && t._1.semester <= tr.to)
	}

	def readShortTopics(s: Set[Section]): Iterable[(String,String,String)] = null
  
    def shortTopicCostudents(s: Set[Section], tr: TimeRange): Iterable[(String, String, String, String, String)]
    		= null

}