package ch.epfl.craft.recom.storage.db
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.model.Student
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.util.TimeRange

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
  
  /* Get Topics belonging to a given section */
  def readTopics(s: Section): Iterable[Topic]
  def readshortTopics(s: Set[Section]): Iterable[(String, String, String)]
  
  /* Import Courses */
  def saveCourses(tl: Iterable[Course])
  
  /* Read a Course */
  def readCourse(cid: String, s: Semester): Option[Course]
  /* Get all the courses of a topic */
  def readCourses(tid: Topic.TopicID): Iterable[Course] = readCourses(tid, TimeRange.all)
  def readCourses(tid: Topic.TopicID, tr: TimeRange): Iterable[Course]
  /* Get all the courses of a section */
  def readCourses(s: Option[Section], tr: TimeRange = TimeRange.all): Iterable[Course]
  
  /* Students */
  def saveStudents(tl: Iterable[Student])
  def readStudent(sid: Student.StudentID): Option[Student]
  
  
}