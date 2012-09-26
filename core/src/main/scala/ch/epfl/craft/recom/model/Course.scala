package ch.epfl.craft.recom.model
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Head

/**
 * A course is the instantiation of a topic. That is, it also contains information about the year,
 * the teacher and any 'concrete' contextual information.
 */

class Course(
    id: String,
    name: String,
    section: Section,
    prerequisites_id: Set[Topic.TopicID],
    description: Option[String],
    val semester: Semester,
    val head: Head
) extends Topic(id, name, section, prerequisites_id,description){
  
  def equals(c: Course) = 	super.equals(c) &&
		  					this.semester == c.semester &&
		  					this.head == c.head
}

object Course{
  
  def apply(t: Topic, s: Semester, h: Head) = 
    new Course(t.id, t.name,t.section,t.prerequisites_id, t.description, s, h)
  
}

