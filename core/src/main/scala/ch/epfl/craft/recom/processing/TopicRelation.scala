package ch.epfl.craft.recom.processing

trait TopicRelation
trait DirectedRelation extends TopicRelation

case class CoStudents(c: Int) extends TopicRelation
case class SemanticDistance(d: Int) extends TopicRelation
case class Complementary() extends TopicRelation
case class Redundant() extends TopicRelation
case class Prerequisite() extends DirectedRelation