package ch.epfl.craft.view.snippet.draw

import net.liftweb.json._

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 04.11.12
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */

case class D3Graph(nodes:List[Node], links:List[Link]) {

  def toJson = {
    val t = nodes.map(_.toJObject)
    val Jnodes = JArray(t)
    val Jlinks = JArray(links.map(_.toJObject))
    val JGraph = JObject(JField("nodes", Jnodes)::JField("links", Jlinks)::Nil)
    JGraph
  }
  
  lazy val maxCostuds = if(links.length > 0) links.maxBy(_.coStudentsN).coStudentsN else 0
}

/**
 * Class representing elements of graph, will be translated into a Json Object used by d3.js
 *
 * @param id the id of the node
 * @param name the complete name of the node
 * @param radius the radius of the circle representing the node
 * @param fill the color of the inside of the node
 * @param numberOfStudents The number of students registered for the class
 */
case class Node(id:String, name:String, radius:Int, fill:RGBColor=RGBColor(255, 255,255), numberOfStudents:Int=0){
  val toJObject = JObject(List(
    JField("id", JString(id)),
    JField("name", JString(name)),
    JField("radius", JInt(radius)),
    JField("fill", fill.toJString),
    JField("numberOfStudents", JInt(numberOfStudents))
  ))
}

/**
 * Case Class representing relation between two elements in graph
 * @param sourceID The ID of the source node
 * @param targetID the ID of the target node
 * @param distance the distance between nodes (length of the link)
 * @param showLink To know if the link has to be shown or not (default is true), this is just related to the visualisation,
 *                 in computation, as soon as the link is created it will be computed (distance constraint)
 * @param coStudentsN The number of co-students of both nodes. This variable is for now somehow similar to the distance.
 */
case class Link(sourceID:String, targetID:String, distance:Int, val coStudentsN:Int, coStudentsPercentage:Int, showLink:Boolean=true){
  val toJObject = JObject(List(
    JField("source", JString(sourceID)),
    JField("target", JString(targetID)),
    JField("distance", JInt(distance)),
    JField("showLink", JBool(showLink)),
    JField("coStudents", JInt(coStudentsN)),
    JField("coStudentsPercentage", JInt(coStudentsPercentage))
  ))
}

/** Utilitary class to represent colors
  *
  * @param red between 0 and 255
  * @param green between 0 and 255
  * @param blue between 0 and 255
  * @param opacity between 0 and 1, set by default to 1
  */

case class RGBColor(red:Int, green:Int, blue:Int, opacity:Double=1){
  def toJString = JString("rgba("+red+","+green+","+blue+","+opacity+")")
}