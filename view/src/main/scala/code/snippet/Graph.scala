package code.snippet

import net.liftweb.json._

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 04.11.12
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */

case class Graph(nodes:List[Node], links:List[Link])
case class Node(id:String, name:String, alias:String, credits:Int){
  val toJObject = JObject(List(
    JField("id", JString(id)),
    JField("name", JString(name)),
    JField("alias",JString(alias)),
    JField("credits", JInt(credits))
  ))
}
case class Link(sourceID:String, targetID:String, value:Int, showLink:Boolean=true){
  val toJObject = JObject(List(
    JField("source", JString(sourceID)),
    JField("target", JString(targetID)),
    JField("value", JInt(value)),
    JField("showLink", JBool(showLink))
  ))
}