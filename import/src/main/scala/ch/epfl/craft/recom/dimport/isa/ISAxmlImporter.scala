package ch.epfl.craft.recom.dimport.isa
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import scala.Option.option2Iterable
import scala.io.Source
import scala.xml.XML
import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.model.administration.Head
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Teacher
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.model.Student
import ch.epfl.craft.recom.model.TakenCourse
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.storage.db.PGDBFactory
import ch.epfl.craft.recom.storage.maps.TopicMap


object ISAxmlImporter extends App {
	
  val dbf = new PGDBFactory("localhost","orgarec","julien","dorloter")
	val s = dbf.store
	/*
  val stud = s.readStudent("179676")
  println(stud)
	*/
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
  
  val fiche_url = "http://isa.epfl.ch/wsa/fiches/FichesCours?" +
  		"invoke=getfiche" +
  		"s&wwXMatiere=" +
  		"&wwXAnneeacad=" +
  		"&wwCSection=" +
  		"&wwCNiveau=" +
  		"&wwXCle=%s"
  
  /*val periods = List("2007-2008","2008-2009","2009-2010","2010-2011","2011-2012","2012-2013")
  val dict = periods.map(p => (p,courseKeyToName(p))).toMap
  */
  /*val ck = courseKeyToName("2011-2012").map(m => m._1)
  println("Saving keys...")
  saveMe(ck,"2011-2012-keys")
  */
  val ck = readMe("2011-2012-keys").asInstanceOf[Iterable[String]]
 /* val all = periods.map{
    p => printf("mapping period %s...\n",p)
      (p,grabCourses(p).map( c => (c._2,grabSubscribed(c._2))))
  }
  println(all)
  */
  /*
  val rel = all.map(p => (p._1,p._2.filter(i => !i._2.isEmpty)))
  saveMe(rel, "2007-2012-dump")
*/
  /*val o = readMe("2007-2012-dump").asInstanceOf[List[(String,Seq[(String, scala.collection.immutable.Seq[(String, String, 
 String, String, String)])])]]
  
  //Only filter out the XCles to grab the credits for courses
  val keys = o.map(_._2).reduce(_ ++ _).map(_._1)
  println("size: " + keys.size)
  */
 /* 
  println("size: " + ck.size)
  var co = 0
  val keysncreds = ck.map{k => 
    val xml = grabFicheXML(k)
    val c = (xml \\ "creditsects").headOption.map(_.text).getOrElse("")
    val t = (k,c)
    co += 1
    println(t + co.toString)
    t
  }
  saveMe(keysncreds, "2007-2012-creds-head")
  */
  var cf = 0
  var co = 0
  val kc = readMe("2007-2012-creds-head").asInstanceOf[Iterable[(String,String)]]
  kc.foreach{
    t => val cred = t._2 match {
      case "" => 0
      case s: String => (new Integer(s)).toInt
    }
    val k = t._1.split("_")(0)
    TopicMap.readMap(k).foreach{
      m => m.credits(cred).save()
      co += 1
    }
    cf += 1
    println(cf.toString + " " + co.toString)
  }
  
  // Get a list of all different sciper numbers...
  //val allScip = o.flatMap(_._2.flatMap(_._2.map(_._1))).toSet

  // For each sciper number, recompose what course was taken, and only keep course data (not subscribed students)
  /*
  println("beginning to map...")
  var c = 1
  val cps = allScip.map{ s => 
    val fil1 = o.map(p => (p._1,p._2.map(c => (c._1,c._2.filter(_._1 == s))).filter(c => !c._2.isEmpty)))
    if (c % 100 == 0) printf("Done %d so far... current:%s \n",c,(s,fil1))

    c+=1
    (s,fil1)
  }
  println("saving...")
  saveMe(cps,"2007-2012-courses-per-sciper")
  println("saved.")
  */
  
  //o.filter{e => !e._2.filter(_._1 == "179676").isEmpty}.map(_._1).foreach(println(_))
  //val fil2 = fil1.map(e =>  (e._1,e._2.map(_._1)))
  /*fil2.foreach{
    p => println(p._1 + ":")
    		p._2.foreach(e => println("\t" + e))
    
  }*/
  /*
  val cps = readMe("2007-2012-courses-per-sciper")
  		.asInstanceOf[Set[(String,List[(String,
  		    Seq[(String, scala.collection.immutable.Seq[
  		          (String, String, String, String, String)])])])]]
  
  val cpsc = cps.map(s => (s._1,s._2.map(l => (l._1,l._2.map(c => (c._1,c._2.map(e => (e._2,e._3,e._5))))))))
  
  saveMe(cpsc,"2007-2012-cps-clean")
  //cps.filter(_._1 == "179676").flatMap(_._2).foreach(println(_))
  */
  		
  /*		
  val data = readMe("2007-2012-cps-clean").asInstanceOf[Set[(String, List[(String, Seq[(String, 
 scala.collection.immutable.Seq[(String, String, String)])])])]]
  
  val resolved = data.map(s =>
    (s._1,s._2.map(p =>
      (p._1,p._2.map(c =>
        (c._1,dict(p._1)(c._1),c._2.head
          ))))))
  saveMe(resolved, "2007-2012-resolved")
  */
  /*
  println("Reading data file...")
  val resolved = readMe("2007-2012-resolved").asInstanceOf[Set[(String, List[(String, Seq[(String, String, (String, String, 
 String))])])]]
  
  resolved.filter(_._1 == "179676").foreach(_._2.foreach{
    p => println(p._1 + ":")
    p._2.foreach(s => println("\t" + s))
  })
  */
  /*
  println("Converting to objects...")
  val objs = resolved.map{ s => 
    val scip = s._1
    val arrival_year = s._2.filter(!_._2.isEmpty).head._1.split("-")(0)
    val arrival_lvl = s._2.filter(!_._2.isEmpty).head._2.headOption.map(c => c._3._2)
    .map(determineStart(_)).get
    
    val arr_sem = AcademicSemester(arrival_lvl,arrival_year,"HIVER")
    
    val sect = s._2.filter(!_._2.isEmpty).head._2.head._3._1
    val last_yr = s._2.last
    val curr_sem = determineCurrent(last_yr)
    
    val sem_hist = extractSem(s._2)
    
    val courses = extractCourses(s._2)
    
    new Student(scip, arr_sem, Some(Section(sect)), curr_sem, sem_hist, courses)
  }
  */
  /*
    objs.filter(_.id == "179676").foreach(_.courses.foreach{
    p => println(p.course.id + ":")
  })
*/
/*
  println("saving objects...")
  s.saveStudents(objs)
  println("done.")
 
  println(s.readStudent("179676"))
  */
  def extractCourses(yrs: List[(String, Seq[(String, String, (String, String, String))])]):
	  Set[TakenCourse] = {
		  yrs.flatMap{ y => 
		    y._2.flatMap{ c => 
		      val cid = c._1.split("_")(0)
		      val n = c._2
		      val sect = c._3._1
		      val pre = Set.empty[Topic.TopicID]
		      val descr = None
		      val creds = None
		      val yt = y._1.split("-")
		      val s = c._3._3 match {
		        case "ETE" => Some(Semester(yt(1),"ETE"))
		        case "HIVER" => Some(Semester(yt(0),"HIVER"))
		        case _ => None
		      }
		      val h = Head.empty
		      
		      // Only return something if the semester is defined.
		      s.map{o => 
		       	val as = AcademicSemester(c._3._2,o)
		        val cours = new Course(cid,n,Section(sect),pre,descr, creds, o, h)
		        TakenCourse(cours,1,None,None,as)}
		    }
		  }.toSet
  }
  
  def extractSem(yrs: List[(String, Seq[(String, String, (String, String, String))])]):
	  Set[AcademicSemester] = 
		  yrs.flatMap{ y => 
		    val s = y._2.map(c => (c._3._2,c._3._3)).toSet
		    val yr = y._1.split("-")
				s.flatMap{ 
		      case (l,"HIVER") => Some(AcademicSemester(l,yr(0),"HIVER"))
		      case (l,"ETE") => Some(AcademicSemester(l,yr(1),"ETE"))
		      case _ => None
		    }
  	}.toSet

  
  def determineStart(lvl: String) = lvl match {
      case "BA1" | "BA2" => "BA1"
      case "BA3" | "BA4" => "BA3"
      case "BA5" | "BA6" => "BA5"

      case "MA1" | "MA2" => "MA1"
      case s: String => s
  }
    
  def determineCurrent(c: (String,Seq[(String,String,(String,String,String))])): Option[AcademicSemester] = {
    if(c._2.isEmpty) None 
    else {
	    val l = c._2.map(t => (t._3._2,t._3._3)).toSet
	    val yrs = c._1.split("-")
	    val h = l.head
	    val lvl = h._1 match {
	      case "BA1" => if(l.size == 2) "BA2" else "BA1"
	      case "BA3" => if(l.size == 2) "BA4" else "BA3"
	      case "BA5" => if(l.size == 2) "BA6" else "BA5"
	      case "MA1" => if(l.size == 2) "MA2" else "MA1"
	      case "H" | "PMH" | "PME" | "E"  => h._1
	      case _ => "Undef"
	    }
	    val yr = h._2 match {
	      case "HIVER" => Some(yrs(0))
	      case "ETE" => Some(yrs(1))
	      case _ => None
	    }
	    yr.map(o => AcademicSemester(lvl,o,h._2))
    }
  }
  
  def courseKeyToName(yr: String): Map[String, String] = {
    val xml = grabCoursesXML(yr)
    val items = xml \\ "item" 
    
    val cl = items.map{ i => 
	    val fstr = (i \ "XCle").text
	    val ckey = fstr
	  	val name = (i \ "XLibelle").text.split(";")(0)
	  	(ckey, name)
    }.toMap
    cl
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
  
  def grabFicheXML(key: String) = XML.loadString(Source.fromURL(fiche_url.format(key)).mkString)
  
  def saveMe(data: Object, fileName: String) = {
    val o = new ObjectOutputStream(new FileOutputStream(fileName))
    o.writeObject(data)
  }
  
  def readMe(fileName: String) = {
    val o = new ObjectInputStream(new FileInputStream(fileName))
    o.readObject
  }
  
}