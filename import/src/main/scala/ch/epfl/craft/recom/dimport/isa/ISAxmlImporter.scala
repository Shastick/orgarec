package ch.epfl.craft.recom.dimport.isa
import scala.io.Source
import scala.xml.NodeSeq
import scala.xml.Unparsed
import scala.xml.XML
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.administration.Teacher
import ch.epfl.craft.recom.model.administration.Head
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.Course
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.FileInputStream

object ISAxmlImporter extends App {
	
  /* need to specify academic period in the form: 2012-2013 */
  val courses_url = "http://isa.epfl.ch/wsa/cles/ClesInscr?" +
  		"invoke=getlistecles" +
  		"&wwXAnneeacad=%s"
  		
  /* need to specify the 'course key' obtained from previous url
   * (format: M784635267_G1378361727_P100304_P106177_P132574)
   */
  val subscribed_url = "http://isa.epfl.ch/wsa/inscr/inscriptions?" +
  		"invoke=getinscriptionsbymatiere" +
  		"&wwXMatiere=" +
  		"&wwXAnneeacad=" +
  		"&wwXSection=" +
  		"&wwXTypesem=" +
  		"&wwXCle=%s"
  	
  val periods = List("2007-2008","2008-2009","2009-2010","2010-2011","2011-2012","2012-2013")
  val all = periods.map{
    p => printf("mapping period %s...\n",p)
      (p,grabCourses(p).map( c => (c._2,grabSubscribed(c._2))))
  }
  
  val rel = all.map(p => (p._1,p._2.filter(i => !i._2.isEmpty)))
  saveMe(rel, "2007-2012-dump")

  val o = readMe("2007-2012-dump").asInstanceOf[List[(String,Seq[(String, scala.collection.immutable.Seq[(String, String, 
 String, String, String)])])]]
  
  val fil1 = o.map(p => (p._1,p._2.filter(c => !c._2.filter(_._1 == "179676").isEmpty)))
  //o.filter{e => !e._2.filter(_._1 == "178684").isEmpty}.map(_._1).foreach(println(_))
  val fil2 = rel.map(e =>  (e._1,e._2.map(_._1)))
  fil2.foreach{
    p => println(p._1 + ":")
    		p._2.foreach(e => println("\t" + e))
    
  }
  def grabSubscribed(key: String) = {
     val studs = grabSubscribedXML(key)
     val items = studs \\ "item"
     val data = items.map{ i =>
       /* Yes, there is a space in the matching string.
        * No, I don't know why it is required to work...*/
       if((i.attributes.head.toString == " xsi:type=\"ns2:inscr_Tinscription\"")) {
	       val scip = (i \ "XSciper").text
	       val misc = i \ "RGps"
	       		val sectionID = (misc \ "RSection" \ "XCode").text
	       		val pedagogicPeriod = (misc \ "RPeriodepedago" \ "XCode").text
	       		val acadPeriod = (misc \ "XPeriodeacad").text
	       		val semesterType = (misc \ "XTypesem").text
	       Some((scip, sectionID, pedagogicPeriod, acadPeriod, semesterType))
       } else None
     }.flatten
     data
  }

  def grabCourses(yr: String) = {
      val xml = grabCoursesXML(yr)
	  val items = xml \\ "item" 
	  
	  val sem = Semester("2012", "fall")
	  
	  val cl = items.map{ i => 
	    val fstr = (i \ "XCle").text
	    val f = fstr.split("_").toList
	  	val lib = (i \ "XLibelle").text.split(";").toIterator
	
	  	val topic = f.find(s =>  s.size > 1 && s.substring(0,1) == "M").map(s =>
	  	  new Topic(s, lib.next, Section(""), Set.empty, None, None))
	    
	  	f.filter(s => s.size > 1 && s.substring(0,1) == "G").foreach(s => lib.next())
	  	
	  	val teachers = f.filter(s => s.size > 1 && s.substring(0,1) == "P").map(s =>
	  	  new Teacher(s, None)).toList
	  	
	  	val head = Head(teachers, List.empty)
	  	
	  	topic.map(t => (Course(t, sem, head), fstr))
	  }.flatten
	  cl
  }
  
  def grabCoursesXML(yr: String) = XML.loadString(Source.fromURL(courses_url.format(yr)).mkString)
  
  def grabSubscribedXML(key: String) = XML.loadString(Source.fromURL(subscribed_url.format(key)).mkString)
  
  def saveMe(data: Object, fileName: String) = {
    val o = new ObjectOutputStream(new FileOutputStream(fileName))
    o.writeObject(data)
  }
  
  def readMe(fileName: String) = {
    val o = new ObjectInputStream(new FileInputStream(fileName))
    o.readObject
  }
  
}