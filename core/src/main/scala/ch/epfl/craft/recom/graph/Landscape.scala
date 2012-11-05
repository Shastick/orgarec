package ch.epfl.craft.recom.graph

import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.storage.db.Storage
import ch.epfl.craft.recom.processing.Processer
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.model.Topic

/**
 * Represents the picture of the topics and what we can learn from the student's history and 
 * evaluations.
 */
class Landscape(
    val SemesterRange: SemesterRange, /* From when to when we want to observe data */
    val section: Set[Section], /* Do we focus on certain sections ? (All sections if empty) */
    val nodes: Set[LandscapeNode], 
    val edges: Set[LandscapeEdge])
    
object Landscape{
  
	def build(s: Storage, p: Processer, tr: SemesterRange,
			se: Set[Section] = Set.empty):Landscape = {
	  
	  val topics = p.readShortTopicsDetailed(se, tr)
	  
	  val costuds = p.readShortTopicCostudents(se, tr)
	  
	  val nodes = topics.map(t =>
	      LandscapeNode(new Topic(t._2.toString,
	          t._1,
	          Section(t._3),
	          Set.empty,
	          Option(t._5),
	          t._4 match {
	          	case 0 => None
	          	case _ => Some(t._4)
	          }),
	          Set(StudentsQuantity(t._6))))
	  
	  val edges = costuds.map(t =>
	    LandscapeEdge(t._1, t._2, Set(CoStudents(t._3.toInt))))
	  
	  new Landscape(tr, se, nodes.toSet, edges.toSet)
	}
}