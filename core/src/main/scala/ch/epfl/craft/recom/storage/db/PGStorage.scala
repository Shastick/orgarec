package ch.epfl.craft.recom.storage.db
import ch.epfl.craft.recom.storage.maps.TopicMap
import ch.epfl.craft.recom.model.Topic
import net.liftweb.db.ConnectionManager
import ch.epfl.craft.recom.storage.maps.StudentMap
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.storage.maps.CourseMap
import net.liftweb.db.ConnectionIdentifier
import ch.epfl.craft.recom.model.Student
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.storage.maps._
import net.liftweb.mapper.By
import ch.epfl.craft.recom.util.TimeRange


class PGStorage(ci: ConnectionIdentifier, db: ConnectionManager) extends Storage {
	
    /* Topics & Courses */
  
  /* Import without linking */
  def importTopics(tl: Iterable[Topic]) = TopicMap.fill(tl)
  /* Link previously imported topics */
  def linkTopics(tl: Iterable[Topic]) = TopicMap.bindFill(tl)

  /* Read a Topic */
  def readTopic(tid: Topic.TopicID): Option[Topic] = TopicMap.read(tid)
  
  def readTopics(se: Section) =
	    TopicMap.findAll(By(TopicMap.section,SectionMap.fill(se))).map(_.read)

  
  /* Import Courses */
  def saveCourses(tl: Iterable[Course]) = CourseMap.fill(tl)
  /* Read a Course */
  def readCourse(cid: Topic.TopicID, s: Semester): Option[Course] = CourseMap.read(cid, s)
  
  def readCourses(tid: Topic.TopicID, tr: TimeRange) = TopicMap.readMap(tid).map(
	    tm => CourseMap.findAll(By(CourseMap.topic,tm)).map(_.read)
	    	.filter(c => c.semester >= tr.from && c.semester <= tr.to)
	  ).getOrElse(List.empty)
	  
  def readCourses(se: Option[Section], tr: TimeRange) = 
    se.map{ s => 
	    val sm = SectionMap.fill(s)
	    TopicMap.findAll(By(TopicMap.section,sm)).flatMap(
	      t => CourseMap.findAll(By(CourseMap.topic,t)).map(_.read)
	      		.filter(c => c.semester >= tr.from && c.semester <= tr.to)
	    )
	  }.getOrElse(List.empty)
  
  /* Students */
  def saveStudents(tl: Iterable[Student]) = StudentMap.fill(tl)
  def readStudent(sid: Student.StudentID): Option[Student] = StudentMap.read(sid)
  
}