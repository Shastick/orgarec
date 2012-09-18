package ch.epfl.craft.recom.items
import ch.epfl.craft.recom.util.Section
import ch.epfl.craft.recom.util.Semester
import ch.epfl.craft.recom.util.Head

/**
 * A course is the instanciation of a topic. That is, it alos contains information about the year,
 * the teacher and any 'concrete' contextual information.
 */

class Course(
    id: String,
    name: String,
    section: Section,
    prerequisites: Set[Topic],
    description: Option[String],
    semester: Semester,
    head: Head
) extends Topic(id, name, section, prerequisites,description){
  
}

