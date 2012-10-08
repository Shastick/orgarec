package ch.epfl.craft.recom.model
import java.text.SimpleDateFormat
import org.specs2.mutable.Specification
import ch.epfl.craft.recom.model.administration._
import ch.epfl.craft.recom.model._
import ch.epfl.craft.recom.storage.db.PGDBFactory
import ch.epfl.craft.recom.storage.maps._
import net.liftweb.mapper.By

class MapSpecs extends Specification {
	
	val dbf = new PGDBFactory("localhost","orgarec","julien","dorloter")
	val s = dbf.store
	
	args(sequential=true)
	
	val d = new SimpleDateFormat("yyyy")
	val y1 = d.parse("2010")
	val y2 = d.parse("2011")
	
	/* Define test objects */
	val (s1, s2, s3, s4) = (Spring(y1), Fall(y1), Spring(y2), Fall(y2))
	val (as1,as2,as3,as4) = (AcademicSemester("BA1",s1),AcademicSemester("BA2",s2),AcademicSemester("BA3",s3),AcademicSemester("BA4",s4))
	val (f1,f2) = (Section("Informatique"),Section("Syscom"))
	val (t1,t2) = (Teacher("André",Some(f2)), Teacher("Jamila",Some(f1)))
	val (a1,a2) = (Assistant("Maxime",Some(f2)),Assistant("Acacio",Some(f1)))
	val (h1,h2) = (Head(List(t1),List(a1)), Head(List(t2),List(a2)))
	val (b1,b2,b3) = (new Topic("cs-3","algorithms",f1,Set("cs-2","cs-1"),Some("Cool Algorithms Design"), Some(5)),
					new Topic("cs-2","programmation",f1,Set("cs-1"),Some("Cool Program Design"), Some(6)),
					new Topic("cs-1","concurrence",f2,Set.empty,Some("Cool Train Design"), Some(4)))
	val topics = Set(b1,b2,b3)
	val (c1,c2,c3) = (Course(b1,s3,h1), Course(b2,s2,h2), Course(b3,s1,h2))
	
	val tc = Set(TakenCourse(c1, 1, None, None,as3), TakenCourse(c2,1,None,None,as2), TakenCourse(c3,1,None,None,as1))
	
	val (e1,e2) = (new Student("179767",as1,Some(f2),Some(as4),Set(as1,as2,as3,as4), tc),
					new Student("200000",as1,Some(f1),Some(as4),Set(as1,as2,as3,as4), tc))
	
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
	  "import & link Topics" in {
	    s.saveTopics(topics); success
	  }
	  
	  "retrieve a Topic" in {
	    val t = s.readTopic(b1.id).get
	    t.id must beEqualTo(b1.id)
	    t.name must beEqualTo(b1.name)
	    t.section must beEqualTo(b1.section)
	    t.prerequisites_id must beEqualTo(b1.prerequisites_id)
	    t.description must beEqualTo(b1.description)
	  }
	}
	
	/* Courses */
	"A CourseMap" should {
	  "save Courses" in {
	    s.saveTopics(Set(c1,c2,c3)); success
	  }
	  "read Course" in {
	    val t = s.readCourse(c1.id,c1.semester).get
	    t.id must beEqualTo(c1.id)
	    t.name must beEqualTo(c1.name)
	    t.section must beEqualTo(c1.section)
	    t.prerequisites_id must beEqualTo(c1.prerequisites_id)
	    t.description must beEqualTo(c1.description)
	    t.semester.equals(c1.semester) must beTrue
	    t.head must beEqualTo(c1.head)
	  }
	}
	
	//Students 
	"A StudentMap" should {
	  "save Students" in {
	    s.saveStudents(Set(e1,e2)); success
	  }
	  "read Students" in {
	    val st = s.readStudent(e1.id).get
	    st.id must beEqualTo(e1.id)
	    st.arrival.equals(e1.arrival) must beTrue
	    st.section must beEqualTo(e1.section)
	    st.currentSemester.get.equals(e1.currentSemester.get) must beTrue
	    st.courses.map(_.course.id) must beEqualTo(e1.courses.map(_.course.id))
	    st.courses.map(_.semester) must beEqualTo(e1.courses.map(_.semester))
	  } 
	}
	
}