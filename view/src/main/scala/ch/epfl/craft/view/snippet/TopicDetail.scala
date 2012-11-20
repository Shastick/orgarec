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
import ch.epfl.craft.recom.processing.Processer
import ch.epfl.craft.recom.storage.db.DBFactory

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
  val proc = DBFactory.processer
  			  
  lazy val costuds = l.coStudents(tid)
      					
  lazy val studCount = meta.collectFirst{case StudentsQuantity(q) => q}
  lazy val sectionRatio = proc.readTopicSectionRatio(tid, l.semesterRange)
  lazy val srNormalized = 
    sectionRatio.map(t => (t._1.name, (t._3*t._2)/sectionRatio.maxBy(_._3)._3))
    .toList.sortBy(_._2).reverse
  			
  def render = {
    "#topic-name *" #> t.map(_.name) &
    "#student-count *" #> studCount.map(_.toString) &
    "#costudents-bar-plot *" #> costudsPlot _ &
    "#sectionratio-bar-plot *" #> sectionRatioPlot _
  }
  
  def costudsPlot(h: NodeSeq) = 
    if(costuds.length > 0)
      h ++ barPlot(
        ViewUtils.tupListInt2RatioCsv(studCount.getOrElse(1.0),l.coStudents(tid,5)),
        "#costudents-bar-plot",
        660, 200)
    else h
    
  def sectionRatioPlot(h: NodeSeq) = 
    h ++ barPlot(
        ViewUtils.tupListDouble2RatioCsv(studCount.getOrElse(1.0),
            srNormalized),
        "#sectionratio-bar-plot",
        660,200)
    //TODO @ julien only do the include once in a clean manner ;-).
  def barPlot(csv: String,id: String, w: Int, h: Int) = 
     <head>{includes}</head> ++ Script(OnLoad(Call("drawBarPlot",
         "name,ratio\n"+csv,
    	id,w,h)))
}