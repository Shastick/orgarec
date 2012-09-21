package ch.epfl.craft.recom.model

trait TopicGroup {
  val topics: Set[Topic]
}

case class Block (
    val topics: Set[Topic]
) extends TopicGroup

case class Specialization (
	val topics: Set[Topic]
) extends TopicGroup

case class Minor (
    val topics: Set[Topic]
) extends TopicGroup

case class Orientation (
    val topics: Set[Topic]
) extends TopicGroup