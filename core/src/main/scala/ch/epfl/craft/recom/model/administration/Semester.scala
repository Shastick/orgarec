package ch.epfl.craft.recom.model.administration
import java.util.Date

/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */

sealed trait Semester{
  val year:Date
}

case class Spring(val year: Date) extends Semester
case class Fall(val year: Date) extends Semester

object Semester {
  def apply(y: Date, s: String): Semester = s.toLowerCase match {
    case "spring" => Spring(y)
    case "fall" => Fall(y)
    case _ => throw new Exception("Bad Semester Specification:" + s)
  }
}