package ch.epfl.craft.recom.model.administration

import org.scala_tools.time.Imports._


/**
 * As everything at EPFL revolves around semesters, lets have a case class representing it...
 */
sealed trait Semester extends Ordered[Semester]{
  val year: DateTime
  
  lazy val year_int = year.getYear
      
  def equals(s: Semester) = {
		if((this.isInstanceOf[Spring] && s.isInstanceOf[Spring]) || 
		  (this.isInstanceOf[Fall] && s.isInstanceOf[Fall])){
    		this.year_int == s.year_int
    } else false
}
  /**
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
  
  lazy val season = this.getClass.getSimpleName
}

case class Spring(val year: DateTime) extends Semester
case class Fall(val year: DateTime) extends Semester

/**
 * Semester builder.
 */
object Semester {
  
  type Identifier = String
  
  val dateformat = "yyyy-MM-dd"
  val f = DateTimeFormat.forPattern(dateformat)
  
  def apply(y: DateTime, s: String): Semester = s.toLowerCase match {
    case "spring" | "ete" => Spring(y)
    case "fall" | "hiver" => Fall(y + 6.months)
    case _ => throw new Exception("Bad Semester Specification:" + s)
  }
  
  def apply(y: LocalDate): Semester = 
    if(y.getMonthOfYear() < 7) apply(y.getYear, "spring")
    else apply(y.getYear(),"fall")
  
  def apply(y: Int, s: String): Semester = apply(new DateTime(y,1,1,0,0),s)
  def apply(y: String, s: String): Semester = apply(new Integer(y),s)
  def apply(y: java.util.Date, s: String): Semester = {
      val c = java.util.Calendar.getInstance
      c.setTime(y)
      apply(new DateTime(c.get(java.util.Calendar.YEAR),1,1,0,0),s)
  }
  def apply(y: LocalDate, s: String): Semester = apply(new DateTime(y),s)

  
  def fromDateString(d: String) = apply(f.parseLocalDate(d))
  

}