package ch.epfl.craft.recom.processing
import ch.epfl.craft.recom.model.administration.Semester

trait TopicRelation
trait DirectedRelation extends TopicRelation

trait CourseRelation extends TopicRelation {
  val semester: Semester
}

case class CoStudents(c: Int, semester: Semester) extends CourseRelation
case class SemanticDistance(d: Int) extends TopicRelation
case class Complementary() extends TopicRelation
case class Redundant() extends TopicRelation
case class Prerequisite() extends DirectedRelation
case class SocialDistance() extends TopicRelation