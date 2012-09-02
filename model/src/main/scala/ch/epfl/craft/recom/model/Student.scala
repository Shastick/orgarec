package ch.epfl.craft.recom.model

class Student(
    val section: Option[Section],
	val currentSemester: Option[Semester],
	
	val currentCourses: Set[CurrentCourse],
    val courseHistory: Set[TookCourse]
){

}

case class TookCourse(course: Course, semester: Semester, count: Int, grade: Int, evaluation: Int)
case class CurrentCourse(course: Course, count: Int)