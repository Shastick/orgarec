package ch.epfl.craft.recom.model
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.AcademicSemester

class Student(
    val id: Student.StudentID,
    val arrival: AcademicSemester,
    val section: Option[Section],
	val currentSemester: Option[AcademicSemester],
	val semesterHistory: Set[AcademicSemester],
	
	val courses: Set[TakenCourse]
)

object Student {
  type StudentID = String
}

case class TakenCourse(course: Course,
					   count: Int,
					   grade: Option[Int],
					   evaluation: Option[Int],
					   semester: AcademicSemester)

