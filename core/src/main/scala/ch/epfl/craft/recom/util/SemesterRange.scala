package ch.epfl.craft.recom.util
import ch.epfl.craft.recom.model.administration.Semester

case class SemesterRange(from: Option[Semester], to: Option[Semester])

object SemesterRange {
  def all = SemesterRange(None,None)
}