package ch.epfl.craft.recom.processing

import net.liftweb.db.ConnectionManager
import net.liftweb.db.ConnectionIdentifier
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.processing.maps.CourseRelationMap
import ch.epfl.craft.recom.storage.maps.CourseMap
import net.liftweb.mapper.By
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.model.Topic

class PGProcesser(ci: ConnectionIdentifier, db: ConnectionManager)
 extends Processer with SQLCallable{
	
	protected val conn = db.newConnection(ci).get

	def costudents_rel = "costudents"
  
	def computeCoStudents = call("computeCoStudents")()
	
	def readShortTopics(s: Set[Section.Identifier]):
	  Iterable[(String, String, String, Int, String)] = 
	    callS[String, String, String, Int, String]("sectionTopics")(s.toList)
	  
	def readShortTopicsDetailed(s: Set[Section.Identifier], sr: SemesterRange):
	  Iterable[(String, String, String, Int, String, Double, Int)] =
	    callS[String, String, String, Int, String, Double, Int]("sectionTopicsWStudentCount")(s.toList, sr)
  
    def readShortTopicCostudents(s: Set[Section.Identifier], tr: SemesterRange):
      Iterable[(String, String, Long)] =
      callS[String, String, Long]("topicCostudents")(s.toList, tr)
  
    def readShortTopicCostudents(s: Set[Section.Identifier],
        tr: SemesterRange, as: Set[AcademicSemester.Identifier]):
	  Iterable[(String, String, Long)] =
	    callS[String, String, Long]("topicCostudents")(s.toList, as.toList, tr)
	
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
	    // TODO : log this instead of throwing lame exceptions... ?
	  }
	}
	
	def readCoStudents(c: Course, tr: SemesterRange = SemesterRange.all) = {
	  val cm = CourseMap.fill(c)
	  val l1 = CourseRelationMap.findAll(By(CourseRelationMap.from,cm),
			  					By(CourseRelationMap.relation,costudents_rel))
			  					.map(rm => (rm.to.map(_.read).get, rm.value.toInt))
						
	  val l2 = CourseRelationMap.findAll(By(CourseRelationMap.to,cm),
			  					By(CourseRelationMap.relation,costudents_rel))
			  					.map(rm => (rm.from.map(_.read).get, rm.value.toInt))
	  (l1 ++ l2).filter(t => t._1.semester >= tr.from && t._1.semester <= tr.to)
	}

	def readTopicSectionRatio(id: Topic.TopicID, r: SemesterRange):
		Iterable[(Section, Double, Int)] =
	  callS[Section, Double, Int]("topicSectionRatio")(id,r).toList
	  
	def readSectionPerTopicDetail(s: Set[Section.Identifier], r: SemesterRange, as: Set[AcademicSemester.Identifier]):
	  Iterable[(Topic.TopicID, String, Section, Double, Double, Int)] =
	    callS[Topic.TopicID, String, Section, Double, Double, Int]("sectionPerTopicDetail")(s.toList, as.toList, r)
	

}