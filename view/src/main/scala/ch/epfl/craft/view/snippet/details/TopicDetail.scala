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
import ch.epfl.craft.view.util.ViewUtils

/**
 * Get the details of a topic in the context of a given landscape
 */
class TopicDetail(a: (Topic.TopicID, Landscape)) {

  val includes = {
        <script language="javascript" type="text/javascript"
		    src="/static/scripts/jquery-1.8.3.min.js"></script>
        <script language="javascript" type="text/javascript"
		    src="/static/scripts/d3/d3.v2.min.js"></script>
        <script language="javascript" type="text/javascript"
		  	src="/static/scripts/barPlotter.js"></script>
  }
  
  val (tid,l) = a
  val (t,meta) = l.nodes.get(tid).map(ln => (Some(ln.node),ln.metadata))
  			.getOrElse((None,Set.empty[TopicMeta]))
  			  
  lazy val costuds = l.coStudents(tid)
      					
  lazy val studCount = meta.collectFirst{case StudentsQuantity(q) => q}
  			
  def render = {
    "#topic-name *" #> t.map(_.name) &
    "#student-count *" #> studCount.map(_.toString) &
    "#costudents-bar-plot *" #> barPlot _
  }
  
  def barPlot(h: NodeSeq) = 
  if(costuds.length > 0)
    h ++ <head>{includes}</head> ++ Script(OnLoad(Call("drawBarPlot",
        "course,ratio\n" + ViewUtils.tupList2RatioCsv(studCount.getOrElse(1.0),l.coStudents(tid,5)),
    	"#costudents-bar-plot",660,200)))
  else h 
}