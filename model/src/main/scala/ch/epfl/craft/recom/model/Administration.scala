package ch.epfl.craft.recom.model
import java.util.Date


object Administration {
  
}

/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */

abstract class Semester(year: Date)

case class Spring(year: Date) extends Semester(year)
case class Fall(year: Date) extends Semester(year)

/**
 * Represent a section
 */
case class Section(name: String)