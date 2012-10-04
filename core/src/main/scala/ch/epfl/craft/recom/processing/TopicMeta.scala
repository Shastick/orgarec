package ch.epfl.craft.recom.processing
import ch.epfl.craft.recom.model.administration.Semester

trait TopicMeta

trait CourseMeta extends TopicMeta {
  val semester: Semester
}

case class StudentsQuantity(c: Int, semester: Semester) extends CourseMeta
case class Difficulty(d: Double, semester: Semester) extends CourseMeta 