package ch.epfl.craft.view.snippet

import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.graph.Landscape
import net.liftweb.util.Helpers._
import ch.epfl.craft.recom.graph.TopicMeta
import ch.epfl.craft.recom.graph.StudentsQuantity
import ch.epfl.craft.view.model.LandscapeHolder

/**
 * Get the details of a topic in the context of a given landscape
 */
class TopicDetail(a: Topic.TopicID) {

  val tid = a
  val l = LandscapeHolder.is
  val (t,meta) = l.flatMap(_.nodes.get(tid).map(ln => (Some(ln.node),ln.metadata)))
  			.getOrElse((None,Set.empty[TopicMeta]))
  
  def render = {
    "#topic-name" #> t.map(_.name) &
    "#student-count" #> meta.collectFirst{case StudentsQuantity(q) => q.toString}
  }
}