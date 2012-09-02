package ch.epfl.craft.recom.model

/**
 * A course is the instanciation of a topic. That is, it alos contains information about the year,
 * the teacher and any 'concrete' contextual information.
 */

class Course(
    name: String,
    section: Section,
    semester: Semester
) extends Topic(name, section){
  
}

