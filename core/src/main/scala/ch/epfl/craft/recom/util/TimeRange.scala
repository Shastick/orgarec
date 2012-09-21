package ch.epfl.craft.recom.util
import ch.epfl.craft.recom.model.administration.Semester

case class TimeRange(from: Option[Semester], to: Option[Semester])