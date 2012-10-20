package ch.epfl.craft.recom.storage.maps
import ch.epfl.craft.recom.model.Student
import ch.epfl.craft.recom.model.TakenCourse
import ch.epfl.craft.recom.storage.maps.assist.Subscribed
import net.liftweb.mapper.By
import net.liftweb.mapper.HasManyThrough
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedInt
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedString

class StudentMap extends LongKeyedMapper[StudentMap] with IdPK{
	def getSingleton = StudentMap
	
	val sciper_len = 6
	
	object sciper extends MappedString(this, sciper_len)
	object arrival extends MappedLongForeignKey(this, AcademicSemesterMap)
	object section extends MappedLongForeignKey(this,SectionMap)
	object currentSemester extends MappedLongForeignKey(this, AcademicSemesterMap)
	
	// Passed and current subscriptions to courses
	// The semester history can be rebuilt from here,
	// so unless we have performance issues we won't do an extra relation for
	// that
	object subscriptions extends HasManyThrough(this, CourseMap, Subscribed,
	    Subscribed.course, Subscribed.student)
	
	def read = StudentMap.fill(this)
}

object StudentMap extends StudentMap with LongKeyedMetaMapper[StudentMap] {
  
  def fill(sl: Iterable[Student]): Iterable[StudentMap] = 
    sl.map(fill _)
  
  def fill(s: Student): StudentMap = {
    val m = StudentMap.findOrCreate(By(StudentMap.sciper,s.id))
    m.sciper(s.id)
    m.arrival(AcademicSemesterMap.fill(s.arrival))
    s.section.foreach(o => m.section(SectionMap.fill(o)))
    s.currentSemester.foreach(o => m.currentSemester(AcademicSemesterMap.fill(o)))
    s.courses.foreach(t => Subscribed.subscribe(m,t))
    m.save
    m
  }
  
  def fill(s: StudentMap): Student = {
    val courses = Subscribed.findAll(By(Subscribed.student, s.id)).map{ sub => 
      TakenCourse(sub.course.map(_.read).get,
          sub.counter,
          Option(sub.grade),
          Option(sub.evaluation), sub.academicSemester.map(_.read).get)
    } toSet
    val history = courses.map(_.semester).toSet
    new Student(s.sciper, s.arrival.map(_.read).get,s.section.map(_.read),s.currentSemester.map(_.read),history,courses)
  }
  
  def read(sid: Student.StudentID): Option[Student] =
    StudentMap.findAll(By(StudentMap.sciper, sid)).headOption.map(_.read)
}