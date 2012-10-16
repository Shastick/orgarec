package code.snippet


import net.liftweb._
import common._
import json._
import util._
import http._
import js._
import SHtml._
import JsCmds._
import scala.None


/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 02.10.12
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
class GraphVisual {
  case class Graph(nodes:List[Node], edges:List[Edge])
  case class Node(id:Int, name:String, alias:String, credits:Int){
    val toJObject = JObject(List(
      JField("id", JInt(id)),
      JField("name", JString(name)),
      JField("alias",JString(alias)),
      JField("credits", JInt(credits))
    ))
  }
  case class Edge(source:Node, target:Node, value:Int){
    val toJObject = JObject(List(
      JField("source", JInt(source.id)),
      JField("target", JInt(target.id)),
      JField("value", JInt(value))
    ))
  }

  val n0 =  Node(100,"Modeles stochastiques pour les communications)", "Mod Stoch",6)
  val n1 =  Node(1,"Principles of digital communications","PDC",6)
  val n2 =  Node(2,"Securité des réseaux","Securité",4)
  val n3 =  Node(3,"Signal processing for communications","Signal proc.",6)
  val n4 =  Node(4,"Compiler construction","Compiler",6)
  val n5 =  Node(5,"Electromagnétisme I : lignes et ondes","EM 1",3)
  val n6 =  Node(6,"Electromagnétisme II : calcul des champs","EM 2",3)
  val n7 =  Node(7,"Electronique II","Elec 2",4)
  val n8 =  Node(8,"Electronique III","Elec 3",3)
  val n9 =  Node(9,"Functional materials in communication systems","FMCS",3)
  val n10 = Node(10,"Graph theory applications","GTA",4)
  val n11 = Node(11,"Informatique du temps réel","ITR",4)
  val n12 = Node(12,"Intelligence artificielle","IA",4)
  val n13 = Node(13,"Internet analytics","Internet",5)
  val n14 = Node(14,"Introduction to computer graphics","Comp Graph",6)
  val n15 = Node(15,"Introduction to database systems","DB",4)
  val n16 = Node(16,"Operating systems","OS",4)
  val n17 = Node(17,"Ressources humaines dans les projets","RES",2)
  val n18 = Node(18,"Software development project","SDP",4)
  val n19 = Node(19,"Software engineering","Sweng",6)


  var nodes = List(n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19)

  var edges = List(
    Edge(n0,n1, 10),
    Edge(n0,n2, 3),
    Edge(n0, n3,8),
    Edge(n0,n10, 5),
    Edge(n1,n2,3),
    Edge(n1,n3,6),
    Edge(n1,n5,1),
    Edge(n2,n3,5),
    Edge(n2,n13,5),
    Edge(n4,n19,5),
    Edge(n5,n6,10),
    Edge(n5,n9,4),
    Edge(n7,n8,10),
    Edge(n8,n9,3),
    Edge(n14,n19,10),
    Edge(n18,n19,7)
  )

  val myGraph = Graph(nodes, edges) 



  def deleteNode = {
    def delete(id:String):JsCmd = {
      nodes = nodes.filterNot(_.id.toString == id);
      JE.JsFunc("graph.removeNode",id.toInt).cmd  &
      ReplaceOptions("node_delete", nodeList, Full(nodeList.head._1))
    }

    def call = ajaxCall(JE.JsRaw("this.value"), delete _)

    def nodeList = ("", " - ")::nodes.map(n => (n.id.toString, n.name))

    SHtml.untrustedSelect(nodeList, Full(nodeList.head._1), delete _,
      "id" -> "node_delete",
      "onchange" -> call._2.toJsCmd,
      "class" -> "input"
    )
  }

  def deleteEdge = {
    val init = ("", " - ")
    var source = ""
    var target = ""

    def isValidEdge(e:Edge, source:String, target:String):Boolean = {
      (e.source.id.toString == source && e.target.id.toString == target)
    }

    def delete:JsCmd = {
      edges = edges.filterNot(isValidEdge(_, source, target))
      JE.JsFunc("graph.removeLink", source.toInt, target.toInt).cmd &
      initializeElements
    }

    def initializeElements ={
      target = ""
      source = ""
      ReplaceOptions("source_edge_delete", sourceNodes, Full(init._1)) &
      ReplaceOptions("target_edge_delete", Nil, Full(init._1)) &
      Run("document.getElementById(\"target_edge_delete\").setAttribute(\"disabled\", \"true\");")
    }

    def sourceNodes: List[(String, String)] = {
      init::nodes.filter(n=> edges.exists(e => e.source == n))
                        .sortWith((x,y) => x.name < y.name)
                        .map(n => (n.id.toString, n.name))
    }

    def targetNodes: List[(String, String)] = {
      nodes.filter(n=> edges.exists(e => e.target == n ))
        .sortWith((x,y) => x.name < y.name)
        .map(n => (n.id.toString, n.name))
    }

    def oppositeSuggestions(source:String): List[(String, String)] = {
      if(source isEmpty) Nil
      else
        init :: (targetNodes.filter(n => edges.exists(isValidEdge(_, source, n._1))))
    }

    def updatedSources(s:String) ={
      source = s
      if (s.nonEmpty){
        ReplaceOptions("target_edge_delete", oppositeSuggestions(s), Full("")) &
        Run("document.getElementById(\"target_edge_delete\").removeAttribute(\"disabled\", 0);")
      } else{
        Run("document.getElementById(\"target_edge_delete\").setAttribute(\"disabled\", \"true\");")
      }
    }

    def updatedTargets(t:String) = {
      target = t;
      if(t.isEmpty)
        Run("document.getElementById(\"target_edge_delete\").setAttribute(\"disabled\", \"true\");")
      else
        Noop
    }

    def callSource = ajaxCall(JE.JsRaw("this.value"), updatedSources _)
    def callTarget = ajaxCall(JE.JsRaw("this.value"), updatedTargets _)

    /* HTML Code  */
    <span> "From: " </span> ++
    SHtml.untrustedSelect(sourceNodes, Full(init._1), source = _,
      "id" -> "source_edge_delete",
      "onchange" -> callSource._2.toJsCmd,
      "width" -> "40px",
      "class" -> "input"
    ) ++
    <span>"To: "</span> ++
    SHtml.untrustedSelect(List(init), Full(init._1) , target = _,
      "id" -> "target_edge_delete",
      "onchange" -> callTarget._2.toJsCmd,
      "width" -> "40px",
      "class" -> "input",
      "disabled" -> "true"
    ) ++
    SHtml.ajaxButton("Confirm", () => delete)
  }
}

object GraphVisual extends GraphVisual {
  /* Get Json representation of the graph */
  def graph2Json = {
    val Jnodes = JArray(nodes.map(_.toJObject))
    val Jedges = JArray(edges.map(_.toJObject))
    val JGraph = JObject(JField("nodes", Jnodes)::JField("links", Jedges)::Nil)
    JGraph
  }

  /* Get Json data to include on left side of screen, details about nodes for now */
  def details2Json(id:Int) = {
    val node = nodes.find(_.id == id)
    val name = if(node.isDefined) node.get.name else "node not defined"
    JString(name)
  }



}