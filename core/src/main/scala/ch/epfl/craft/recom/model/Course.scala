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
    prerequisites: Set[Topic],
    description: Option[String],
    val semester: Semester,
    val head: Head
) extends Topic(id, name, section, prerequisites,description){
  
}

