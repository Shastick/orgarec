package ch.epfl.craft.recom.graph

import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.storage.db.Storage
import ch.epfl.craft.recom.processing.Processer
import ch.epfl.craft.recom.model.Course
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.administration.AcademicSemester

/**
 * Represents the picture of the topics and what we can learn from the student's history and 
 * evaluations.
 */
class Landscape(
    val semesterRange: SemesterRange, /* From when to when we want to observe data */
    val sections: Set[Section], /* Do we focus on certain sections ? (All sections if empty) */
    val levels: Set[AcademicSemester], /* Focus on certain levels only ? */
    val nodes: Map[Topic.TopicID,LandscapeNode], 
    val edges: Map[(Topic.TopicID, Topic.TopicID),LandscapeEdge]){
  
  /* Get Edges connected to given topic */
  def connectedEdges(tid: Topic.TopicID) = edges.collect {
    case ((from,to),edge) if (from == tid) => (to,edge)
    case ((from,to),edge) if (to == tid) => (from,edge)
  }
  
  def coStudents(tid: Topic.TopicID, lim: Int): List[(Topic.TopicID,Int)] = 
    coStudents(tid).slice(0,lim)
    
  def coStudents(tid: Topic.TopicID): List[(Topic.TopicID,Int)] = 
    connectedEdges(tid).flatMap(t => t._2.relations
      					.collectFirst{case CoStudents(c) => (t._1,c)})
      					.toList.sortBy(_._2).reverse
  
}
    
object Landscape{
  
	def empty = new Landscape(SemesterRange.all, Set.empty, Set.empty, Map.empty, Map.empty)
	
	def build(s: Storage, p: Processer,
			tr: SemesterRange,
			se: Set[Section] = Set.empty,
			as: Set[AcademicSemester] = Set.empty):Landscape = {
	  
	  val topics = p.readShortTopicsDetailed(se.map(_.name), tr)
	  
	  val costuds = p.readShortTopicCostudents(se.map(_.name), tr, as.map(_.level))
	  
	  val nodes = topics.map(t =>
	      (t._2,
	          LandscapeNode(new Topic(t._2.toString,
	          t._1,
	          Section(t._3),
	          Set.empty,
	          Option(t._5),
	          t._4 match {
	          	case 0 => None
	          	case _ => Some(t._4)
	          }),
	          Set(StudentsQuantity(t._6)))))
	  
	  val edges = costuds.map(t =>
	    ((t._1,t._2),LandscapeEdge(t._1, t._2, Set(CoStudents(t._3.toInt)))))
	  
	  new Landscape(tr, se, as, nodes.toMap, edges.toMap)
	}
}