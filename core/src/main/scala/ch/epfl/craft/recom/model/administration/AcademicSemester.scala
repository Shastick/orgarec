package ch.epfl.craft.recom.model.administration
import java.util.Date

sealed trait AcademicSemester extends Semester {
  val semester: Semester
  override def year = semester.year
}
case class BA1(s: Semester) extends AcademicSemester
case class BA2(s: Semester) extends AcademicSemester
case class BA3(s: Semester) extends AcademicSemester
case class BA4(s: Semester) extends AcademicSemester
case class BA5(s: Semester) extends AcademicSemester
case class BA6(s: Semester) extends AcademicSemester

case class MA1(s: Semester) extends AcademicSemester
case class MA2(s: Semester) extends AcademicSemester
case class MA3(s: Semester) extends AcademicSemester

case class H(s: Semester) extends AcademicSemester

object AcademicSemester {
  def apply(lvl: String, sem: Semester) = lvl.toUpperCase match {
    case "BA1" => BA1(sem)
    case "BA2" => BA2(sem)
    case "BA3" => BA3(sem)
    case "BA4" => BA4(sem)
    case "BA5" => BA5(sem)
    case "BA6" => BA6(sem)
    case "MA1" => MA1(sem)
    case "MA2" => MA2(sem)
    case "MA3" => MA3(sem)
    case "H" => H(sem)
  }
}