package ch.epfl.craft.recom.model.administration
import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */

sealed trait Semester{
  val year:Date
  
  def equals(s: Semester) = {
		if((this.isInstanceOf[Spring] && s.isInstanceOf[Spring]) || 
		  (this.isInstanceOf[Fall] && s.isInstanceOf[Fall])){
    		val ct = Calendar.getInstance; ct.setTime(this.year)
    		val cs = Calendar.getInstance; cs.setTime(s.year)
    		ct.get(Calendar.YEAR) == cs.get(Calendar.YEAR)
    } else false
}
    
  def season: String = 	if(this.isInstanceOf[Spring]) "spring" 
	  					else "fall"
}

case class Spring(val year: Date) extends Semester
case class Fall(val year: Date) extends Semester

object Semester {
  
  val df = new SimpleDateFormat("yyyy")
  
  def apply(y: Date, s: String): Semester = s.toLowerCase match {
    case "spring" => Spring(y)
    case "fall" => Fall(y)
    case _ => throw new Exception("Bad Semester Specification:" + s)
  }
  
  def apply(y: String, s: String): Semester = apply(df.parse(y),s)
}