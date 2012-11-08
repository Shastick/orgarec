package ch.epfl.craft.view.snippet

import net.liftweb.json._

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 04.11.12
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */

case class Graph(nodes:List[Node], links:List[Link])
case class Node(id:String, name:String, alias:String, radius:Int, fill:RGBColor=RGBColor(255, 0,0)){
  val toJObject = JObject(List(
    JField("id", JString(id)),
    JField("name", JString(name)),
    JField("radius", JInt(radius)),
    JField("fill", fill.toJString)
  ))
}
case class Link(sourceID:String, targetID:String, distance:Int, showLink:Boolean=true){
  val toJObject = JObject(List(
    JField("source", JString(sourceID)),
    JField("target", JString(targetID)),
    JField("distance", JInt(distance)),
    JField("showLink", JBool(showLink))
  ))
}

case class RGBColor(red:Int, green:Int, blue:Int, opacity:Double=1){
  def toJString = JString("rgb("+red+","+green+","+blue+")")
}