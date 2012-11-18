package ch.epfl.craft.view.snippet

import net.liftweb._
import common._
import json._
import http._
import js._
import js.JE.{Call, JsVar, JsRaw}
import JsCmds._
import xml.NodeSeq
import ch.epfl.craft.view.model._
import ch.epfl.craft.recom.graph.StudentsQuantity
import ch.epfl.craft.view.util.ViewUtils


/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 02.10.12
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */

class GraphVisual {

  var deletedNodes:List[Node] = Nil
  var deletedLinks:List[Link] = Nil

  def lsh = LandscapeHolder
  def ls = lsh.current
  
  val myGraph = lsh.getJsGraph()
  val nodes = myGraph.nodes
  val links = myGraph.links

  def displayableNodes = nodes.diff(deletedNodes)
  def displayableLinks = links.diff(deletedLinks)

  var currentCoStudThreshold = 60
  deletedLinks = links.filter(_.coStudents > currentCoStudThreshold)

  def updateLinkThreshold = {
    def updateThresh(cs:Int) = {
      val commands =
        if( cs > currentCoStudThreshold) {
          val toDelete = displayableLinks.filter(_.coStudents<cs)
          deletedLinks ++= toDelete
          toDelete.map(link => JE.JsFunc("graph.removeLink", link.sourceID, link.targetID).cmd)
        } else {
          val toAdd = deletedLinks.filter(_.coStudents>cs)
          deletedLinks = deletedLinks.diff(toAdd)
          toAdd.map(link => JE.JsFunc("graph.addLink", link.toJObject).cmd)
        }
      currentCoStudThreshold = cs
      commands
    }
    SHtml.ajaxSelectElem[Int](List(10,20, 30, 40, 50, 60, 70, 80, 90, 100), Full(currentCoStudThreshold))(updateThresh(_))
  }

  def getGraph = JsRaw(
    "function getGraph(succName) {" +
      SHtml.ajaxCall(JsVar("succName"),
          fname => Call(fname, ViewUtils.graph2Json(D3Graph(displayableNodes,displayableLinks))
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

  def contextMenuContent(id:String) = {
    val node = nodes.find(_.id ==id)
    if(node.isDefined)
      <h3>{node.get.name}</h3> ++
        <span> <b>credits: </b>{node.get.radius/4}</span>
    else NodeSeq.Empty
  }

  def updateContextMenu = JsRaw(
    "function updateContextMenu(nodeID) {" +
      SHtml.ajaxCall(JsVar("nodeID"), id => SetHtml("context_menu", contextMenuContent(id)))._2.toJsCmd
      + "}"
  )

  def getInteractions = Script(getGraph & getDetails & updateContextMenu)
}