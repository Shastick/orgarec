package ch.epfl.craft.view.snippet

import scala.xml.NodeSeq
import ch.epfl.craft.recom.graph.{TopicMeta, StudentsQuantity}
import ch.epfl.craft.view.model._
import ch.epfl.craft.view.util.ViewUtils
import net.liftweb.http.SHtml
import net.liftweb.http.StatefulSnippet
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JE.Call
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JE.JsVar
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js._
import net.liftweb.common.Full
import scala.xml.Elem
import ch.epfl.craft.view.model.UserDisplay
import ch.epfl.craft.view.snippet.details.BarPlot
import ch.epfl.craft.recom.storage.db.DBFactory

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 02.10.12
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */

class GraphInteractions extends StatefulSnippet {
  
  def graph = LandscapeHolder.d3Graph
  def ls = LandscapeHolder.landscape

  def nodes = graph.nodes
  def links = graph.links
  
  var coStudThreshold = 80


  def dispatch = {case "render" => render}
  
  def render =  {
    UserDisplay.reset(nodes,links.filter(_.coStudents > coStudThreshold))
    "#generated-ajax *" #> <head>{getInteractions}</head>   &
    "#threshold-updater *" #> ( <span>Update Threshold</span> ++ sliderThresh )
  }
  /*
  def updateLinkThreshold = {
    SHtml.ajaxSelectElem[Int](Range(0,graph.maxCostuds,10).toList, Full(coStudThreshold))(updateThresh(_))
  } */

  def updateThresholdSlider = JsRaw(
    "function updateThreholdSlider(value) {" +
      SHtml.ajaxCall(JsVar("value"), value =>
        //SetHtml("context_menu", contextMenuContent(value)))._2.toJsCmd
        updateThresh(value.toInt)
      )
      + "}"
  )

  def sliderThresh = {
    val id= "threshold-slider"
    val min =0
    val max = graph.maxCostuds
    val cb = SHtml.ajaxCall(JsRaw("ui.value"), value => updateThresh(value.toInt))
    val script = Script(new JsCmd {
      def toJsCmd = "$(function() {"+
        "$(\"#"+ id +"\").slider({ "+
          //"range: false, "+
          "min: "+ 0 +", "+
          "max: "+ graph.maxCostuds +", " +
          "value: " + coStudThreshold + ", " +
          "change: function(event, ui) {" +
            //"updateValues(event,ui);" +
            cb._2.toJsCmd +
          "}," +
          "slide: function(event, ui) {" +
            //"updateValues(event,ui);" +
          "}" +
        "});" +
        "});"
    })
    if (max>min) {
      val labelDiv = {<div style="height: 10px"><span id="value1" class="sliderLabel">{min}</span><span id="value2" class="sliderLabel" style="left: 96%">{max}</span></div>}
      val sliderDiv = {<div style="margin-top: 10px;" id={id}></div>}
      {script} ++ <div class="sliderBarContainer">{labelDiv}{sliderDiv}</div>
    } else {
      <i> {min} </i>
    }
  }

  def updateThresh(cs: Int) = {
    val commands =
      if(cs > coStudThreshold) {
        val toDelete = UserDisplay.links.filter(_.coStudents < cs)
        UserDisplay.removeLinks(toDelete)
        toDelete.map(link => JE.JsFunc("graph.removeLink", link.sourceID, link.targetID).cmd)
      } else {
        val toAdd = links.filter(_.coStudents > cs).diff(UserDisplay.links)
        UserDisplay.addLinks(toAdd)
        toAdd.map(link => JE.JsFunc("graph.addLink", link.toJObject).cmd)
      }
    coStudThreshold = cs
    commands
  }

  def getGraph = JsRaw(
    "function getGraph(succName) {" +
      SHtml.ajaxCall(JsVar("succName"),
          fname => Call(fname, UserDisplay.render))
          ._2.toJsCmd
      + "}"
  )

  /* Get Json data to include on left side of screen, details about nodes for now */
  def nodeDetails(id:String): NodeSeq = {
    ls.nodes.get(id).map{ n => 
    <h1>{n.node.name}</h1>
    <span>{n.node.section.name} section</span><br/>
    ++ n.node.credits.map(c => <span>{c} Credits</span>)
    }.getOrElse(NodeSeq.Empty)
  }
  
  def coStudSubGraph(id: String): NodeSeq = {
    val lim = ls.nodes(id).metadata.collectFirst{case StudentsQuantity(q) => q}
    BarPlot.ratioFromIntTup(lim.getOrElse(1.0),
              ls.coStudents(id,5).map(t => (ls.nodes(t._1).node.name,t._2)),
              "#costudents-subgraph-data",
              500,200).draw
  }

  def sectionSubGraph(id: String): NodeSeq = {
    val tid = ls.nodes(id).node.id
    val l = ls
    //val (tid,l) = a
    val (t,meta) = l.nodes.get(tid).map(ln => (Some(ln.node),ln.metadata))
      .getOrElse((None,Set.empty[TopicMeta]))
    val proc = DBFactory.processer

    lazy val sectionRatio = proc.readTopicSectionRatio(tid, l.semesterRange)
    lazy val studCount = meta.collectFirst{case StudentsQuantity(q) => q}
    lazy val srNormalized =
      sectionRatio.map(t => (t._1.name, (t._3*t._2)/sectionRatio.maxBy(_._3)._3))
        .toList.sortBy(_._2).reverse//.slice(0,5)

    BarPlot.ratioFromDoubleTup(studCount.getOrElse(1.0),
      srNormalized,
      "#sectionratio-subgraph-data",
      500, 200).draw
  }

  def getDetails = JsRaw(
    "function getDetails(nodeID) {" +
      SHtml.ajaxCall(JsVar("nodeID"), id => {
        SetHtml("detail-data", nodeDetails(id)) &
        SetHtml("costudents-subgraph-data", coStudSubGraph(id))  &
        SetHtml("sectionratio-subgraph-data", sectionSubGraph(id))
        })._2.toJsCmd
      + "}"
  )

  def getInteractions = Script(getGraph & getDetails)

}
