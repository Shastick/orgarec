package ch.epfl.craft.recom.model
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester

class Student(
    val id: String,
    val arrival: Semester,
    val section: Option[Section],
	val currentSemester: Option[Semester],
	val semesterHistory: List[Semester],
	
	val courses: Set[TakenCourse]
){
  
}

case class TakenCourse(course: Course, count: Int, grade: Option[Int], evaluation: Option[Int])

