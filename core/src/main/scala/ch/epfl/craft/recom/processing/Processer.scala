package ch.epfl.craft.recom.processing
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.AcademicSemester

/** 
 * The processer trait holds everything that 'will probably take some time' to be done and, generally,
 * what is not computed/analyzed/processed at insert or view time.
 */
trait Processer {
  /**
   * Compute and save the number of co-students between each courses.
   */
  def computeCoStudents(): Unit
  
  def readCoStudents(c1: Course, c2: Course): Int
  
  /**
   * Returns a tuple containing all the courses having co-students with this course
   * and the corresponding quantity of co-students.
   */
  def readCoStudents(c: Course, tr: SemesterRange = SemesterRange.all): Iterable[(Course, Int)]
  
  /**
   * Get the short topic (prerequisites empty) data belonging to the mentioned sections.
   * A returned tuple is: (<Topic Name>,<Topic ISA ID>,<Section Name>,<Credits>,<Descripption>)
   */
  def readShortTopics(s: Set[Section.Identifier]):
	  Iterable[(String, String, String, Int, String)]
  /**
   * A returned tuple is: (<Topic Name>,<Topic ISA ID>,<Section Name>,<Credits>,<Descripption>,<StudentCount>)
   */
  def readShortTopicsDetailed(s: Set[Section.Identifier], sr: SemesterRange):
	  Iterable[(String, String, String, Int, String,Int)]
  
  /**
   * A returned tuple is: (<Topic1 ISA ID>,<Topic2 ISA ID>,<costudents count>)
   */
  def readShortTopicCostudents(s: Set[Section.Identifier], tr: SemesterRange):
	  Iterable[(String, String, Long)]
  /**
   * Variant with academic semester set to consider 
   */
  def readShortTopicCostudents(s: Set[Section.Identifier], tr: SemesterRange, as: Set[AcademicSemester.Identifier]):
	  Iterable[(String, String, Long)]
}