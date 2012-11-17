package ch.epfl.craft.view.snippet

import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.graph.Landscape
import net.liftweb.util.Helpers._
import ch.epfl.craft.recom.graph.TopicMeta
import ch.epfl.craft.recom.graph.StudentsQuantity
import ch.epfl.craft.view.model.LandscapeHolder
import ch.epfl.craft.recom.graph.LandscapeEdge
import ch.epfl.craft.recom.graph.CoStudents
import scala.xml.NodeSeq
import net.liftweb.http.js.JE.Call
import net.liftweb.http.js.JsCmds.OnLoad
import net.liftweb.http.js.JsCmds.Script
import net.liftweb.http.js.JsCmds.jsExpToJsCmd

/**
 * Get the details of a topic in the context of a given landscape
 */
class TopicDetail(a: (Topic.TopicID, Landscape)) {

  val includes = {
        <script language="javascript" type="text/javascript"
		    src="/static/scripts/jquery-1.8.2.min.js"></script>
        <script language="javascript" type="text/javascript"
		    src="/static/scripts/d3/d3.v2.min.js"></script>
        <script language="javascript" type="text/javascript"
		  	src="/static/scripts/barPlotter.js"></script>
  }
  
  val (tid,l) = a
  val (t,meta) = l.nodes.get(tid).map(ln => (Some(ln.node),ln.metadata))
  			.getOrElse((None,Set.empty[TopicMeta]))
  			
  lazy val eoi = l.edges.collect {
    case ((from,to),edge) if (from == tid) => (to,edge)
    case ((from,to),edge) if (to == tid) => (from,edge)
  }
  
  lazy val costuds = eoi.flatMap(t => t._2.relations
      					.collectFirst{case CoStudents(c) => (t._1,c)})
      					.toList.sortBy(_._2).reverse
      					
  lazy val studCount = meta.collectFirst{case StudentsQuantity(q) => q}
  			
  def render = {
    "#topic-name *" #> t.map(_.name) &
    "#student-count *" #> studCount.map(_.toString) &
    "#costudents-bar-plot *" #> barPlot _
  }
  
  private def topCostudCourses(q: Int) = if(costuds.size > q) costuds.slice(0,q)
		  						 else costuds
		  						 
  //TODO @ julien handle cases where several courses are considered and the ratio might get over 1
  def tupList2RatioCsv(tl: List[(String, Int)]) = 
    tl.map(t => 
      l.nodes.get(t._1).map(_.node.name).getOrElse(t._1) + ","
      + studCount.map(d => t._2/d).getOrElse(0.0).toString)
    .reduce(_ + "\n" +_)
  
  def barPlot(h: NodeSeq) = 
  if(costuds.length > 0)
    h ++ includes ++ Script(OnLoad(Call("drawBarPlot",
        "course,ratio\n" + tupList2RatioCsv(topCostudCourses(5)),
    	"#costudents-bar-plot")))
  else h 
}