package ch.epfl.craft.view.snippet

import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.graph.Landscape
import net.liftweb.util.Helpers._
import ch.epfl.craft.recom.graph.TopicMeta
import ch.epfl.craft.recom.graph.StudentsQuantity
import ch.epfl.craft.view.model.LandscapeHolder
import ch.epfl.craft.recom.graph.LandscapeEdge
import ch.epfl.craft.recom.graph.CoStudents

/**
 * Get the details of a topic in the context of a given landscape
 */
class TopicDetail(a: (Topic.TopicID, Landscape)) {

  val (tid,l) = a
  val (t,meta) = l.nodes.get(tid).map(ln => (Some(ln.node),ln.metadata))
  			.getOrElse((None,Set.empty[TopicMeta]))
  			
  lazy val eoi = l.edges.collect{
    case ((from,to),edge) if (from == tid) => (to,edge)
    case ((from,to),edge) if (to == tid) => (from,edge)
  }
  
  lazy val costuds = eoi.flatMap(t => t._2.relations
      					.collectFirst{case CoStudents(c) => (t._1,c)})
      					.toList.sortBy(_._2).reverse
  			
  def render = {
    println(l.edges)
    "#topic-name *" #> t.map(_.name) &
    "#student-count *" #> meta.collectFirst{case StudentsQuantity(q) => q.toString} &
    "#costudents-bar-plot *" #> topCostudCourses(5).map(_.toString())
  }
  
  def topCostudCourses(q: Int) = if(costuds.size > q) costuds.slice(0,q-1)
		  						 else costuds
}