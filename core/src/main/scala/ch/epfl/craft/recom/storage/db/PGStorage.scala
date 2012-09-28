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


class PGStorage(ci: ConnectionIdentifier, db: ConnectionManager) extends Storage {
	
    /* Topics & Courses */
  
  /* Import without linking */
  def importTopics(tl: Iterable[Topic]) = TopicMap.fill(tl)
  /* Link previously imported topics */
  def linkTopics(tl: Iterable[Topic]) = TopicMap.bindFill(tl)

  /* Read a Topic */
  def readTopic(tid: Topic.TopicID): Option[Topic] = TopicMap.read(tid)
  
  /* Import Courses */
  def saveCourses(tl: Iterable[Course]) = CourseMap.fill(tl)
  /* Read a Course */
  def readCourse(cid: Topic.TopicID, s: Semester): Option[Course] = CourseMap.read(cid, s)
  
  /* Students */
  def saveStudents(tl: Iterable[Student]) = StudentMap.fill(tl)
  def readStudent(sid: Int): Option[Student] = StudentMap.read(sid)
  
}