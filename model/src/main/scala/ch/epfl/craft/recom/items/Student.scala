package ch.epfl.craft.recom.items
import ch.epfl.craft.recom.util.Semester
import ch.epfl.craft.recom.util.Section

class Student(
    val arrival: Semester,
    val section: Option[Section],
	val currentSemester: Option[Semester],
	
	val currentCourses: Set[CurrentCourse],
    val courseHistory: Set[TookCourse]
){

}

case class TookCourse(course: Course, semester: Semester, count: Int, grade: Int, evaluation: Int)
case class CurrentCourse(course: Course, count: Int)