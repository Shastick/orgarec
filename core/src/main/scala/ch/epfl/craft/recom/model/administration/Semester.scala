package ch.epfl.craft.recom.model.administration
import java.util.Date

/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */

sealed trait Semester{
  val year:Date
  def equals(s: Semester) = //TODO check if equals should override something...
    if((this.isInstanceOf[Spring] && s.isInstanceOf[Spring]) ||
        (this.isInstanceOf[Fall] && s.isInstanceOf[Fall]))
    		this.year == s.year
    else false
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