package ch.epfl.craft.recom.model.administration
import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */

sealed trait Semester extends Ordered[Semester]{
  val year: Date
  
  lazy val year_int = {
	val c = Calendar.getInstance()
    c.setTime(this.year)
    c.get(Calendar.YEAR)}
    
  def equals(s: Semester) = {
		if((this.isInstanceOf[Spring] && s.isInstanceOf[Spring]) || 
		  (this.isInstanceOf[Fall] && s.isInstanceOf[Fall])){
    		this.year_int == s.year_int
    } else false
}
  /*
   * Compare will give the 'semester' distance between two semesters :
   * 	eg: Fall(2012) - Spring(2012) will be 1,
   * 		Fall(2012) - Fall(2013) will be -1
   */
  def compare(that: Semester): Int = {
    val d = this.year_int - that.year_int
    if((this.isInstanceOf[Spring] && that.isInstanceOf[Spring]) ||
      (this.isInstanceOf[Fall] && that.isInstanceOf[Fall]))
    	return 2*d
    else if(this.isInstanceOf[Fall]) 2*d + 1
    else 2*d - 1
  }
  
  def >=(that: Option[Semester]): Boolean = that match {
    case None => true
    case Some(s) => this >= s
  }
  
  def <=(that: Option[Semester]): Boolean = that match{
    case None => true
    case Some(s) => this <= s
  }
  
  def season: String = 	if(this.isInstanceOf[Spring]) "spring" 
	  					else "fall"
}

case class Spring(val year: Date) extends Semester
case class Fall(val year: Date) extends Semester


object Semester {
  
  val df = new SimpleDateFormat("yyyy")
  
  def apply(y: Date, s: String): Semester = s.toLowerCase match {
    case "spring" | "ete" => Spring(y)
    case "fall" | "hiver" => Fall(y)
    case _ => throw new Exception("Bad Semester Specification:" + s)
  }
  
  def apply(y: String, s: String): Semester = apply(df.parse(y),s)
}