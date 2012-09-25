package ch.epfl.craft.recom.model.administration

sealed trait Staff {
  val name: String
  val section: Option[Section]
}
case class Teacher(name: String, section: Option[Section]) extends Staff
case class Assistant(name: String, section: Option[Section]) extends Staff

object Staff {
  def apply(n: String, s: Option[Section], role: String) = role.toLowerCase() match {
    case "teacher" => Teacher(n,s)
    case "assistant" => Assistant(n,s)
    case _ => throw new Exception("Bad Staff specification:" + role)
  }
}