package ch.epfl.craft.recom.graph
import ch.epfl.craft.recom.model.administration.Semester

trait TopicMeta

case class StudentsQuantity(c: Int) extends TopicMeta
case class Difficulty(d: Double) extends TopicMeta