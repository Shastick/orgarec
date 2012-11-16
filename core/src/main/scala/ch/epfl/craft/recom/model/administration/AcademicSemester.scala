package ch.epfl.craft.recom.model.administration

import org.scala_tools.time.Imports._

sealed trait AcademicSemester {
  val semester: Option[Semester]
  val year = semester.map(_.year)
  
  def equals(s: AcademicSemester) = 
		if(this.getClass.getSimpleName == s.getClass.getSimpleName){
    		this.year == s.year
    } else false
  
  lazy val level = this.getClass.getSimpleName

}
case class BA1(semester: Option[Semester]) extends AcademicSemester
case class BA2(semester: Option[Semester]) extends AcademicSemester
case class BA3(semester: Option[Semester]) extends AcademicSemester
case class BA4(semester: Option[Semester]) extends AcademicSemester
case class BA5(semester: Option[Semester]) extends AcademicSemester
case class BA6(semester: Option[Semester]) extends AcademicSemester

case class MA1(semester: Option[Semester]) extends AcademicSemester
case class MA2(semester: Option[Semester]) extends AcademicSemester
case class MA3(semester: Option[Semester]) extends AcademicSemester

case class H(semester: Option[Semester]) extends AcademicSemester
case class PMH(semester: Option[Semester]) extends AcademicSemester
case class PME(semester: Option[Semester]) extends AcademicSemester
case class E(semester: Option[Semester]) extends AcademicSemester
case class Undef(semester: Option[Semester]) extends AcademicSemester


object AcademicSemester {
  
  type Identifier = String
  
  def apply(lvl: String, year: String, season: String): AcademicSemester = 
    apply(lvl, Semester(year,season))
  
  def apply(lvl: String, sem: Semester): AcademicSemester = apply(lvl, Some(sem))
  def apply(lvl: String, sem: Option[Semester]): AcademicSemester = lvl.toUpperCase match {
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