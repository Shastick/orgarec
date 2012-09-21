package ch.epfl.craft.recom.model

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

