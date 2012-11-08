package ch.epfl.craft.recom.graph
import ch.epfl.craft.recom.model.administration.Semester

trait TopicMeta

case class StudentsQuantity(avg: Double) extends TopicMeta
case class Difficulty(d: Double) extends TopicMeta