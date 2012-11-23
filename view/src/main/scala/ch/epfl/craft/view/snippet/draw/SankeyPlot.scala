package ch.epfl.craft.view.snippet.draw

import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE.Call
import net.liftweb.json.JsonAST.JObject
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.model.administration.Section
import net.liftweb.json.JsonAST.JArray
import net.liftweb.json.JsonAST.JField
import net.liftweb.json.JsonAST.JString
import net.liftweb.json.JsonAST.JInt
import net.liftweb.json.JsonAST.JDouble


class SankeyPlot(jGraph: JObject) {
	
  lazy val draw = Script(OnLoad(Call("drawSankeyPlot",
         jGraph,"#drawMe")))
}

object SankeyPlot {
  
  def fromSectionPerTopicDetail(
      data: List[(Topic.TopicID, String, Section, Double, Double, Int)]) = {
    val (nodeMap, nodes) = {
      val sources = data.map(t => (t._3.name,t._3.name)).distinct
	  val dest = data.map(t => (t._1.toString,t._2)).distinct
	  val nodes = sources ++ dest
	  
	  val num = Range(0, nodes.size).toList
	  
	  (nodes.map(n => n._1).zip(num).toMap,nodes)
   }
    val Jnodes = JArray(nodes.map(t => JObject(List(JField("id",JString(t._1)),JField("name",JString(t._2))))).toList)
    
    val links = JArray(data.map{ t => 
      JObject(List(
	      JField("source", JInt(nodeMap(t._3.name))),
	      JField("target", JInt(nodeMap(t._1))),
	      JField("value", JDouble(t._4))))
      }.toList)
      
    new SankeyPlot(JObject(JField("nodes", Jnodes)::JField("links", links)::Nil))
  }
}