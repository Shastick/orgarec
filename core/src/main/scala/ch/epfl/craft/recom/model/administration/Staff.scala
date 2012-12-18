package ch.epfl.craft.recom.model.administration

/**
 * Represent staff (teachers, assistants,...)
 */
sealed trait Staff {
  val name: String
  val section: Option[Section]
  
  def equals(s: Staff) = this.name.toLowerCase == s.name.toLowerCase
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