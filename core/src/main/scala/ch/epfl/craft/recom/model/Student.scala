package ch.epfl.craft.recom.model
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.AcademicSemester

/**
 * Represent a single student, with his section and full course history until now.
 */
class Student(
    val id: Student.StudentID,
    val arrival: AcademicSemester,
    val section: Option[Section],
	val currentSemester: Option[AcademicSemester],
	val semesterHistory: Set[AcademicSemester],
	
	val courses: Set[TakenCourse]
    
) {
      override def toString = ("Student: %s\n" +
    		"\t arrival: %s\n" +
    		"\t section: %s\n" +
    		"\t currentSemester: %s\n" +
    		"\t history: %s\n" +
    		"\t courses: %s\n").format(id, arrival, section, currentSemester, semesterHistory, printCourses(courses))
      
      def printCourses(cs: Set[TakenCourse]): String = {
        cs.map{ tc => 
          "\n\t\t " + tc.course.name + ", " + tc.semester.toString
        }.reduce(_ + _)
      }
}

object Student {
  type StudentID = String
}

/**
 * A TakenCourse links a student to a course and holds relevant meta-data.
 */
case class TakenCourse(course: Course,
					   count: Int,
					   grade: Option[Int],
					   evaluation: Option[Int],
					   semester: AcademicSemester)

