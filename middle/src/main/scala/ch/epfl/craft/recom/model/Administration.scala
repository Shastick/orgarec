package ch.epfl.craft.recom.model
import java.util.Date


object Administration {
  
}

/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */

sealed trait Semester{
  val year:Date
}

case class Spring(val year: Date) extends Semester
case class Fall(val year: Date) extends Semester

/**
 * Represent a section
 */
case class Section(name: String)

/**
 * Represent whoever is responsible for a course (one or several teachers, assistants, etc)
 * Listing the assistants 'because we can' (or, can we ?)
 */
case class Head(teachers: List[Teacher], assistants: List[Assistant])

sealed trait Staff {
  val name: String
  val section: Section
}
case class Teacher(name: String, section: Section) extends Staff
case class Assistant(name: String, section: Section) extends Staff