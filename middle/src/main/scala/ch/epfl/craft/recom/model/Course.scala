package ch.epfl.craft.recom.model

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

