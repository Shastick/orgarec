package ch.epfl.craft.recom.storage.db
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.model.Student
import ch.epfl.craft.recom.model.administration.Semester

trait Storage {
  
  /* Topics & Courses */
  
  /* Import without linking */
  def importTopics(tl: Iterable[Topic])
  /* Link previously imported topics */
  def linkTopics(tl: Iterable[Topic])
  /* Import & linking */
  def saveTopics(tl: Iterable[Topic]) = {
    importTopics(tl)
    linkTopics(tl)
  }
  /* Read a Topic */
  def readTopic(tid: Topic.TopicID): Option[Topic]
  
  /* Import Courses */
  def saveCourses(tl: Iterable[Course])
  /* Read a Course */
  def readCourse(cid: String, s: Semester): Option[Course]
  
  /* Students */
  def saveStudents(tl: Iterable[Student])
  def readStudent(sid: Student.StudentID): Option[Student]
  
  
}