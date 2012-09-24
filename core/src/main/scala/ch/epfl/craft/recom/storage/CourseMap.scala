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

class CourseMap extends LongKeyedMapper[CourseMap] with IdPK {
	
	def getSingleton = CourseMap
	
	val name_len = 200
	val isa_id_len = 20
	val descr_len = 10000
	val season_len = 10
	
	object isa_id extends MappedString(this, isa_id_len)
	object name extends MappedString(this, name_len)
	object section extends MappedLongForeignKey(this,SectionMap)
	object description extends MappedString(this, descr_len)
	object semester extends MappedLongForeignKey(this,SemesterMap)
	object topic extends MappedLongForeignKey(this, TopicMap)

	// Prerequisites
	object prerequisites extends HasManyThrough(this, CourseMap, Prerequisite,
	    Prerequisite.required,Prerequisite.course)
	// Head(s)
	object teachers extends HasManyThrough(this, StaffMap, Teaches,
	    Teaches.teacher, Teaches.course)
	object assistants extends HasManyThrough(this, StaffMap, Assists, Assists.assistant, Assists.course)
	
	def read = CourseMap.fill(this)
}

object CourseMap extends CourseMap with LongKeyedMetaMapper[CourseMap] {
  
  def fill(c: CourseMap): Course = {
    null
  }
  
  def fill(c: Course): CourseMap = {
	val cm = CourseMap.findAll(By(CourseMap.isa_id,c.id)).headOption.getOrElse(CourseMap.create.isa_id(c.id))
	cm.name(c.name)
	  .section(SectionMap.fill(c.section))
	  .semester(SemesterMap.fill(c.semester))
	
	Teaches.setTeachersFor(c, cm)
	Assists.setAssistsFor(c, cm)
	
	Prerequisite.setPrerequisites(c)

	c.description.foreach(cm.description(_))
	cm.save
	cm
  }
}