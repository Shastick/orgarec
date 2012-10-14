package code.snippet

import net.liftweb._
import json._
import util._
import http.js._

import net.liftweb.util.JsonCmd
import net.liftweb.common._
import Helpers._
import http._
import js.JsCmds._
import rest.RestHelper


/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 02.10.12
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
object GraphVisual {
  case class Graph(nodes:List[Node], edges:List[Edge])
  case class Node(order:Int, name:String, alias:String, credits:Int){
    val toJObject = JObject(List(
      JField("order", JInt(order)),
      JField("name", JString(name)),
      JField("alias",JString(alias)),
      JField("credits", JInt(credits))
    ))
  }
  case class Edge(source:Node, target:Node, value:Int){
    val toJObject = JObject(List(
      JField("source", JInt(source.order)),
      JField("target", JInt(target.order)),
      JField("value", JInt(value))
    ))
  }

  val n0 =  Node(0,"Modeles stochastiques pour les communications)", "Mod Stoch",6)
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


  val nodes = List(n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19)

  val edges = List(
    Edge(n0,n1, 10),
    Edge(n0,n2, 3),
    Edge(n0, n3,8),
    Edge(n0,n10, 5),
    Edge(n1,n2,2),
    Edge(n1,n3,6),
    Edge(n1,n5,2),
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

  def graph2Json = {
    val Jnodes = JArray(nodes.map(_.toJObject))
    val Jedges = JArray(edges.map(_.toJObject))
    val JGraph = JObject(JField("nodes", Jnodes)::JField("links", Jedges)::Nil)
    JGraph
  }

 def getJson = JsCrVar("graph",graph2Json).toJsCmd
  
  def showGraph = {
    <head>
      <script type="text/javascript" src="static/scripts/d3/d3.v2.js"></script>
      <script type="text/javascript" src="static/scripts/jquery-1.8.2.min.js"></script>
      <script type="text/javascript" src="static/scripts/jquery.tipsy.js"></script>
      <link rel="stylesheet" type="text/css" href="static/css/d3-test.css" />
      <link rel="stylesheet" type="text/css" href="static/css/tipsy.css" />
    </head> ++
      <h3>Bachelor 5/6 - Communication Systems</h3> ++
      <!-- <div id="data-container">
        <script type="text/javascript">{JsCrVar("graph",graph2Json).toJsCmd}</script>
      </div>  -->
      <div id="graph_and_infos" class="graph_wrapper" width=" 800px">
        <div id="controls-container" class="left_box">

        </div>
        <div id="graph-container" class="main_box">
          <!--<script type="text/javascript">{JsCrVar("graph",graph2Json).toJsCmd}</script>-->
          <script type="text/javascript" src="static/scripts/study_plan_display_lift.js"></script>
        </div>
        <div id="info-container" class="right_box">
          Bonjour!
        </div>
      </div>

  }
}

object MyGraphApi extends RestHelper {
  serve {
    case Req("graph" :: Nil, _, GetRequest)  => {
      JsonResponse(GraphVisual.graph2Json)
    }
  }
}