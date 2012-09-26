package ch.epfl.craft.recom.model
import org.specs2.mutable.Specification
import ch.epfl.craft.recom.storage._
import ch.epfl.craft.recom.model.administration._
import java.text.SimpleDateFormat
import net.liftweb.mapper.By
import ch.epfl.craft.recom.storage.db.PGDBFactory
import net.liftweb.mapper.DB
import net.liftweb.db.DefaultConnectionIdentifier

class MapSpecs extends Specification {
	
	val dbf = new PGDBFactory("localhost","orgarec","julien","dorloter")

	args(sequential=true)
	
	val d = new SimpleDateFormat("yyyy")
	val y1 = d.parse("2010")
	val y2 = d.parse("2011")
	
	/* Define test objects */
	val (s1, s2, s3, s4) = (Spring(y1), Fall(y1), Spring(y2), Fall(y2))
	val (f1,f2) = (Section("Informatique"),Section("Syscom"))
	val (t1,t2) = (Teacher("André",Some(f2)), Teacher("Jamila",Some(f1)))
	val (a1,a2) = (Assistant("Maxime",Some(f2)),Assistant("Acacio",Some(f1)))
	val (h1,h2) = (Head(List(t1),List(a1)), Head(List(t2),List(a2)))
	val (b1,b2,b3) = (new Topic("cs-3","algorithms",f1,Set("cs-2","cs-1"),Some("Cool Algorithms Design")),
					new Topic("cs-2","programmation",f1,Set("cs-2","cs-1"),Some("Cool Program Design")),
					new Topic("cs-1","concurrence",f2,Set("cs-2","cs-1"),Some("Cool Train Design")))
	val topics = Set(b1,b2,b3)
	val (c1,c2,c3) = (Course(b1,s3,h1), Course(b2,s2,h2), Course(b3,s1,h2))
	
	val tc = Set(TakenCourse(c1, 1, None, None), TakenCourse(c2,1,None,None), TakenCourse(c3,1,None,None))
	
	val (e1,e2) = (new Student(179767,s1,Some(f2),Some(s4),Set(s1,s2,s3,s4), tc),
					new Student(200000,s1,Some(f1),Some(s4),Set(s1,s2,s3,s4), tc))
	
	/* Semesters */
	"A SemesterMap" should {
	  "save Semesters" in {
	    SemesterMap.fill(Set(s1,s2,s3,s4)); success
	  }
	  "read a Semester" in {
	    SemesterMap.readMap(s2).map(_.read) must beEqualTo(Some(s2))
	  }
	}
	
	/* Sections */
	"A SectionMap" should {
	  "save Sections" in {
	    SectionMap.fill(Set(f1,f2)); success
	  }
	  "read a Section" in {
	    SectionMap
	    	.findAll(By(SectionMap.name, f1.name))
	    	.headOption.map(_.read) must beEqualTo(Some(f1))
	  }
	}
	
	/* Staff */
	"A StaffMap" should {
	  "save Staffs" in {
	    StaffMap.fill(Set(t1,t2,a1,a2));success
	  }
	  "read a Teacher" in {
	    StaffMap.read(t1.name) must beEqualTo(Some(t1))
	  }
	  "read an Assistant" in {
	    StaffMap.read(a1.name) must beEqualTo(Some(a1))
	  }
	}
		
	/* Topics */
	"A  TopicMap" should {
	  "save Topics without setting prerequisites" in {
	    TopicMap.fill(topics); success
	  }
	  
	  "setting Topics prerequisites" in {
	    topics.foreach(TopicMap.bindFill _); success
	  }
	  
	  "retrieve a Topic" in {
	    TopicMap.read(b1.id) must beEqualTo(Some(b1))
	  }
	}
	
	/* Courses */
	"A CourseMap" should {
	  "save Courses" in {
	    CourseMap.fill(Set(c1,c2,c3)); success
	  }
	  "read Course" in {
	    CourseMap.read(c1.id, c1.semester) must beEqualTo(Some(c1))
	  }
	}
	
	/* Students */
	"A StudentMap" should {
	  "save Students" in {
	    StudentMap.fill(Set(e1,e2)); success
	  }
	  "read Students" in {
	    StudentMap.read(e1.id) must beEqualTo(e1)
	  }
	}
	
}