package ch.epfl.craft.recom.util
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

/**
 * Represent whoever is responsible for a course (one or several teachers, assistants, etc)
 * Listing the assistants 'because we can' (or, can we ?)
 */
case class Head(teachers: List[Teacher], assistants: List[Assistant])

case class Teacher(name: String, section: Section)
case class Assistant(name: String, section: Section)