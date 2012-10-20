package ch.epfl.craft.recom.model.administration
import java.util.Date
import java.util.Calendar

sealed trait AcademicSemester {
  val semester: Semester
  val year = semester.year
  
  def equals(s: AcademicSemester) = 
		if(this.getClass.getSimpleName == s.getClass.getSimpleName){
    		val ct = Calendar.getInstance; ct.setTime(this.year)
    		val cs = Calendar.getInstance; cs.setTime(s.year)
    		ct.get(Calendar.YEAR) == cs.get(Calendar.YEAR)
    } else false
}
case class BA1(semester: Semester) extends AcademicSemester
case class BA2(semester: Semester) extends AcademicSemester
case class BA3(semester: Semester) extends AcademicSemester
case class BA4(semester: Semester) extends AcademicSemester
case class BA5(semester: Semester) extends AcademicSemester
case class BA6(semester: Semester) extends AcademicSemester

case class MA1(semester: Semester) extends AcademicSemester
case class MA2(semester: Semester) extends AcademicSemester
case class MA3(semester: Semester) extends AcademicSemester

case class H(semester: Semester) extends AcademicSemester
case class PMH(semester: Semester) extends AcademicSemester
case class PME(semester: Semester) extends AcademicSemester
case class E(semester: Semester) extends AcademicSemester
case class Undef(semester: Semester) extends AcademicSemester


object AcademicSemester {
  
  def apply(lvl: String, year: String, season: String): AcademicSemester = 
    apply(lvl, Semester(year,season))
  
  
  def apply(lvl: String, sem: Semester): AcademicSemester = lvl.toUpperCase match {
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
    case "PMH" => PMH(sem)
    case "PME" => PME(sem)
    case "E" => E(sem)
    case _ => Undef(sem)
  }
}