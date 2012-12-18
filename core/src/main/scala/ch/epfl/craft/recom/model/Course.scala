package ch.epfl.craft.recom.model
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Head

/**
 * A course is the instantiation of a [[ch.epfl.craft.recom.model.Topic]]. That is, it also contains information about the year,
 * the teacher and any 'concrete' contextual information.
 */
class Course(
    id: String,
    name: String,
    section: Section,
    prerequisites_id: Set[Topic.TopicID],
    description: Option[String],
    credits: Option[Int],
    val semester: Semester,
    val head: Head
) extends Topic(id, name, section, prerequisites_id, description, credits){
  
  def equals(c: Course) = this.id == c.id
  def equivalent(c: Course) = super.equivalent(c) &&
		  					this.semester == c.semester &&
		  					this.head == c.head
		  					
  def topic = new Topic(id, name, section, prerequisites_id, description, credits)
}

object Course{
  def apply(t: Topic, s: Semester, h: Head) = 
    new Course(t.id, t.name,t.section,t.prerequisites_id, t.description, t.credits, s, h)
  
}

