package ch.epfl.craft.recom.processing
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.util.TimeRange

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
  def readCoStudents(c: Course, tr: TimeRange = TimeRange.all): Iterable[(Course, Int)]

}