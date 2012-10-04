package ch.epfl.craft.recom.processing

trait TopicMeta

case class StudentsQuantity(total: Int, avg: Double) extends TopicMeta
case class Difficulty(d: Double) extends TopicMeta