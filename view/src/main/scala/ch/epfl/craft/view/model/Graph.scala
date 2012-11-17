package ch.epfl.craft.view.model

import net.liftweb.json._

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 04.11.12
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */

case class Graph(nodes:List[Node], links:List[Link])

/**
 * Class representing elements of graph, will be translated into a Json Object used by d3.js
 *
 * @param id the id of the node
 * @param name the complete name of the node
 * @param radius the radius of the circle representing the node
 * @param fill the color of the inside of the node
 * @param strokeWidthCategory the stroke category (Integers between 1 and 3 will be treated)
 */
case class Node(id:String, name:String, radius:Int, fill:RGBColor=RGBColor(255, 255,255), strokeWidthCategory:Int=1){
  val toJObject = JObject(List(
    JField("id", JString(id)),
    JField("name", JString(name)),
    JField("radius", JInt(radius)),
    JField("fill", fill.toJString),
    JField("strokeWidthCategory", JInt(strokeWidthCategory))
  ))
}
case class Link(sourceID:String, targetID:String, distance:Int, showLink:Boolean=true, coStudents:Int){
  val toJObject = JObject(List(
    JField("source", JString(sourceID)),
    JField("target", JString(targetID)),
    JField("distance", JInt(distance)),
    JField("showLink", JBool(showLink)),
    JField("coStudents", JInt(coStudents))
  ))
}

case class RGBColor(red:Int, green:Int, blue:Int, opacity:Double=1){
  def toJString = JString("rgba("+red+","+green+","+blue+","+opacity+")")
}