package code.snippet


import net.liftweb._
import common._
import json._
import http._
import js._
import SHtml._
import JsCmds._
import js.jquery.JqJE._
import ch.epfl.craft.recom._
import xml.NodeSeq


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

  //val myGraph = SampleGraph.graph
  val myGraph =  StudyPlanCompleteGraph.graph
  val nodes = myGraph.nodes
  val links = myGraph.links

  def displayableNodes = nodes.diff(deletedNodes)
  def displayableLinks = links.diff(deletedLinks)

  def deleteNode = {
    def delete(id:String):JsCmd = {
      //nodes = nodes.filterNot(_.id == id);
      nodes.find(_.id==id).foreach(n => deletedNodes = n :: deletedNodes)
      JE.JsFunc("graph.removeNode",id).cmd  &
      ReplaceOptions("node_delete", nodeList, Full(nodeList.head._1))
    }

    def call = ajaxCall(JE.JsRaw("this.value"), delete _)

    def nodeList = ("", " - ")::displayableNodes
        .sortWith((x,y)=>x.name<y.name)
        .map(n => (n.id, n.name))

    SHtml.untrustedSelect(nodeList, Full(nodeList.head._1), delete _,
      "id" -> "node_delete",
      "onchange" -> call._2.toJsCmd,
      "class" -> "input"
    )
  }

  def deleteLink = {
    val init = ("", " - ")
    var source = ""
    var target = ""

    def isValidLink(e:Link, source:String, target:String):Boolean = {
      (e.sourceID== source && e.targetID == target)
    }

    def delete:JsCmd = {
      //links = links.filterNot(isValidLink(_, source, target))
      displayableLinks.find(l => l.sourceID == source && l.targetID == target).foreach(l => deletedLinks = l :: deletedLinks)
      JE.JsFunc("graph.removeLink", source, target).cmd &
      initializeElements
    }

    def initializeElements ={
      target = ""
      source = ""
      ReplaceOptions("source_link_delete", sourceNodes, Full(init._1)) &
      ReplaceOptions("target_link_delete", Nil, Full(init._1)) &
      (JqId("target_link_delete")~> JqAttr("disabled", "disabled")).cmd &
      (JqId("button_link_delete")~> JqAttr("disabled", "disabled")).cmd
    }

    def sourceNodes: List[(String, String)] = {
      init::displayableNodes
        .filter(n=> links.exists(e => e.sourceID == n.id))
        .sortWith((x,y) => x.name < y.name)
        .map(n => (n.id, n.name))
    }

    def targetNodes: List[(String, String)] = {
      displayableNodes.filter(n=> links.exists(e => e.targetID == n.id ))
        .sortWith((x,y) => x.name < y.name)
        .map(n => (n.id, n.name))
    }

    def oppositeSuggestions(source:String): List[(String, String)] = {
      if(source isEmpty) Nil
      else
        init :: (targetNodes.filter(n => displayableLinks.exists(isValidLink(_, source, n._1))))
    }

    def updatedSources(s:String) ={
      source = s
      if (s.nonEmpty){
        ReplaceOptions("target_link_delete", oppositeSuggestions(s), Full("")) &
        Run("document.getElementById(\"target_link_delete\").removeAttribute(\"disabled\", 0);")
      } else{
        (JqId("target_link_delete")~> JqAttr("disabled", "disabled")).cmd
      }
    }

    def updatedTargets(t:String) = {
      target = t;
      if(t.isEmpty)
        (JqId("target_link_delete")~> JqAttr("disabled", "disabled")).cmd  &
        (JqId("button_link_delete")~> JqAttr("disabled", "disabled")).cmd
      else
        Run("document.getElementById(\"button_link_delete\").removeAttribute(\"disabled\", 0);")
    }

    def callSource = ajaxCall(JE.JsRaw("this.value"), updatedSources _)
    def callTarget = ajaxCall(JE.JsRaw("this.value"), updatedTargets _)

    /* HTML Code  */
    <span> "From: " </span> ++
    SHtml.untrustedSelect(sourceNodes, Full(init._1), source = _,
      "id" -> "source_link_delete",
      "onchange" -> callSource._2.toJsCmd,
      "width" -> "40px",
      "class" -> "input"
    ) ++
    <span>"To: "</span> ++
    SHtml.untrustedSelect(List(init), Full(init._1) , target = _,
      "id" -> "target_link_delete",
      "onchange" -> callTarget._2.toJsCmd,
      "width" -> "40px",
      "class" -> "input",
      "disabled" -> "true"
    ) ++
    SHtml.ajaxButton("Confirm", () => delete, "id" -> "button_link_delete", "disabled" -> "true")
  }

  def editNode = {
    def edit = JE.JsFunc("graph.editNode", Node("100","Modeles stochastiques pour les communications)", "Mod Stoch",12).toJObject ).cmd
    SHtml.ajaxButton("Edit Node", () => edit)
  }

  def selectSemesters = {
     NodeSeq.Empty
  }


}

object GraphVisual extends GraphVisual {
  def apply = new GraphVisual

  /* Get Json representation of the graph */
  def graph2Json = {
    val Jnodes = JArray(displayableNodes.map(_.toJObject))
    val Jlinks = JArray(displayableLinks.map(_.toJObject))
    val JGraph = JObject(JField("nodes", Jnodes)::JField("links", Jlinks)::Nil)
    JGraph
  }

  /* Get Json data to include on left side of screen, details about nodes for now */
  def details2Json(id:String) = {
    val node = nodes.find(_.id == id)
    val name = if(node.isDefined) node.get.name else "node not defined"
    JString(name)
  }
}