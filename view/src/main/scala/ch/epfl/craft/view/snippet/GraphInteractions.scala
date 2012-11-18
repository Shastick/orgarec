package ch.epfl.craft.view.snippet

import scala.xml.NodeSeq
import ch.epfl.craft.recom.graph.StudentsQuantity
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

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 02.10.12
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */

class GraphInteractions extends StatefulSnippet {
  
  var graph = LandscapeHolder.getD3Graph
  def nodes = graph.nodes
  def links = graph.links
  
  var coStudThreshold = 80
  
  var shownNodes: List[Node] = nodes
  var shownLinks: List[Link] = links.filter(_.coStudents > coStudThreshold)
  
  def ls = LandscapeHolder.current
  
  def dispatch = {case "render" => render}

  
  def render = 
    "#threshold-updater *" #> (updateLinkThreshold ++ <head>{getInteractions}</head>)
    
  def updateLinkThreshold: Elem = {
    def updateThresh(cs: Int) = {
      val commands =
        if(cs > coStudThreshold) {
          val toDelete = shownLinks.filter(_.coStudents < cs)
          toDelete.map(link => JE.JsFunc("graph.removeLink", link.sourceID, link.targetID).cmd)
        } else {
          val toAdd = links.filter(_.coStudents > cs).diff(shownLinks)
          toAdd.map(link => JE.JsFunc("graph.addLink", link.toJObject).cmd)
        }
      coStudThreshold = cs
      commands
    }
    SHtml.ajaxSelectElem[Int](Range(0,101,10).toList, Full(coStudThreshold))(updateThresh(_))
  }

  def getGraph = JsRaw(
    "function getGraph(succName) {" +
      SHtml.ajaxCall(JsVar("succName"),
          fname => Call(fname, D3Graph(shownNodes,shownLinks).toJson
              ))._2.toJsCmd
      + "}"
  )

  /* Get Json data to include on left side of screen, details about nodes for now */
  def nodeDetails(id:String):NodeSeq = {
    val node = nodes.find(_.id == id)
    println("-----> id: "+id)
    val name = if(node.isDefined) node.get.name else "node not defined"
    <span>{name}</span>
  }
  
  def nodeSubGraph(id: String): NodeSeq = {
    val lim = ls.nodes(id).metadata.collectFirst{case StudentsQuantity(q) => q}
    Script(OnLoad(Call("drawBarPlot",
        "course,ratio\n" + 
          ViewUtils.tupList2RatioCsv(lim.getOrElse(1.0),
              ls.coStudents(id,5).map(t => (ls.nodes(t._1).node.name,t._2))),
    	"#subgraph-data",500,200)))
  }

  def getDetails = JsRaw(
    "function getDetails(nodeID) {" +
      SHtml.ajaxCall(JsVar("nodeID"), id => {
        SetHtml("detail-data", nodeDetails(id)) &
        SetHtml("subgraph-data", nodeSubGraph(id))
        })._2.toJsCmd
      + "}"
  )

  def contextMenuContent(id:String) =
    ls.nodes.get(id).map{
      t => <h3>{t.node.name}</h3>
        <span><b>credits: </b>{t.node.credits}</span>
    }.getOrElse(NodeSeq.Empty)

  def updateContextMenu = JsRaw(
    "function updateContextMenu(nodeID) {" +
      SHtml.ajaxCall(JsVar("nodeID"), id =>
        SetHtml("context_menu", contextMenuContent(id)))._2.toJsCmd
      + "}"
  )

  def getInteractions = Script(getGraph & getDetails & updateContextMenu)
}