package ch.epfl.craft.recom.model.administration
import java.util.Date
import java.util.Calendar

/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */

sealed trait Semester{
  val year:Date
  
  def equals(s: Semester) = {
		if((this.isInstanceOf[Spring] && s.isInstanceOf[Spring]) || 
		  (this.isInstanceOf[Fall] && this.isInstanceOf[Fall])){
    		val ct = Calendar.getInstance; ct.setTime(this.year)
    		val cs = Calendar.getInstance; cs.setTime(s.year);
    		ct.get(Calendar.YEAR) == cs.get(Calendar.YEAR)
    } else false
}
    
  def season: String = 	if(this.isInstanceOf[Spring]) "spring" 
	  					else "fall"
}

case class Spring(val year: Date) extends Semester
case class Fall(val year: Date) extends Semester

object Semester {
  def apply(y: Date, s: String): Semester = s.toLowerCase match {
    case "spring" => Spring(y)
    case "fall" => Fall(y)
    case _ => throw new Exception("Bad Semester Specification:" + s)
  }
}