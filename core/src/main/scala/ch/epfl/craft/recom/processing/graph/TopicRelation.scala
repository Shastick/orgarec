package ch.epfl.craft.recom.processing.graph
import ch.epfl.craft.recom.model.administration.Semester

trait TopicRelation
trait DirectedRelation extends TopicRelation

case class CoStudents(c: Int, semester: Semester) extends TopicRelation
case class SemanticDistance(d: Int) extends TopicRelation
case class Complementary() extends TopicRelation
case class Redundant() extends TopicRelation
case class Prerequisite() extends DirectedRelation
case class SocialDistance() extends TopicRelation