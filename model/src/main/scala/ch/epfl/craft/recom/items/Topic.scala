package ch.epfl.craft.recom.items
import ch.epfl.craft.recom.util.Section


/**
 * A topic is the abstract of a course, that is, a course without the information about the 
 * teacher or the year it is given
 */
class Topic(
    id: String,
    name: String,
    section: Section,
    prerequisites: Set[Topic]
){

}