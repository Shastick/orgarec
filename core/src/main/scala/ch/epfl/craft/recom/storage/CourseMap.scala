package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedDate
import net.liftweb.mapper.HasManyThrough
import ch.epfl.craft.recom.storage.assist.Prerequisite
import ch.epfl.craft.recom.storage.assist.Teaches
import ch.epfl.craft.recom.storage.assist.Assists
import ch.epfl.craft.recom.model.Course
import net.liftweb.mapper.By
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Head
import ch.epfl.craft.recom.model.administration.Semester

class CourseMap extends LongKeyedMapper[CourseMap] with IdPK {
	
	def getSingleton = CourseMap
	
	object semester extends MappedLongForeignKey(this,SemesterMap)
	object topic extends MappedLongForeignKey(this, TopicMap)

	// Prerequisites
	object prerequisites extends HasManyThrough(this, TopicMap, Prerequisite,
	    Prerequisite.required,Prerequisite.topic)
	// Head(s)
	object teachers extends HasManyThrough(this, StaffMap, Teaches,
	    Teaches.teacher, Teaches.course)
	object assistants extends HasManyThrough(this, StaffMap, Assists, Assists.assistant, Assists.course)
	
	def read = CourseMap.fill(this)
}

object CourseMap extends CourseMap with LongKeyedMetaMapper[CourseMap] {
  
  def fill(cl: TraversableOnce[Course]): TraversableOnce[CourseMap] =
    cl.map(fill _)
  
  //TODO @julien cleanup this pouerkzz
  def fill(c: CourseMap): Course = 
    c.topic.map{ tm => 
      val t = TopicMap.fill(tm)
      val s = c.semester.map(SemesterMap.fill(_)).getOrElse(throw new Exception("Undefined Semester"))
      val teachers = c.teachers.get.map(_.read)
      val assists = c.assistants.get.map(_.read)
      new Course(t.id, t.name, t.section, t.prerequisites_id, t.description, s, Head(teachers, assists))
  }.get
  
  def fill(c: Course): CourseMap = {
    val sm = SemesterMap.fill(c.semester)
    val tm = TopicMap.bindFill(c)
    
	val cm = CourseMap.findOrCreate(By(CourseMap.semester,sm),By(CourseMap.topic,tm))

	cm.semester(SemesterMap.fill(c.semester))
	cm.topic(tm)
	
	Teaches.setTeachersFor(c, cm)
	Assists.setAssistsFor(c, cm)
	
	cm.save
	cm
  }
  
  def read(id: Topic.TopicID, s: Semester): Option[Course] = 
    TopicMap.readMap(id) match {
    	case Some(tm) => CourseMap.findAll( By(CourseMap.topic,tm),
    										By(CourseMap.semester,SemesterMap.readMap(s).getOrElse(SemesterMap.create)))
    										.headOption.map(_.read)
    	case None => None
  	}  
}