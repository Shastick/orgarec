package ch.epfl.craft.recom.util
import ch.epfl.craft.recom.model.administration.Semester

/**
 * Semester Ranges are usted to define what time span we are considering.
 * 
 * 'None' means no (upper or lower) limit.
 */
case class SemesterRange(from: Option[Semester], to: Option[Semester])

object SemesterRange {
  def all = SemesterRange(None,None)
}