package ch.epfl.craft.recom.model
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.storage.TopicMap


/**
 * A topic is the abstract of a course, that is, a course without the information about the 
 * teacher or the year it is given
 */
class Topic(
    val id: String,
    val name: String,
    val section: Section,
    val prerequisites_id: Set[Topic.TopicID],
    val description: Option[String]
){
	def equals(c: Topic) = 	c.id == this.id && 
							this.name == c.name &&
							this.prerequisites_id == c.prerequisites_id &&
							this.section == c.section &&
							this.description == c.description
	
	lazy val prerequisites = prerequisites_id.flatMap{ tid =>
	  TopicMap.read(tid)
	}
}

object Topic{
  type TopicID = String
}