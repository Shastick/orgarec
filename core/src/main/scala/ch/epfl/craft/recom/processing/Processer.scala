package ch.epfl.craft.recom.processing

/** 
 * The processer trait holds everything that 'will probably take some time' to be done and, generally,
 * what is not computed/analyzed/processed at insert or view time.
 */
trait Processer {
  /**
   * Compute and save the number co-students between each courses.
   */
  def computeCoStudents(): Unit

}