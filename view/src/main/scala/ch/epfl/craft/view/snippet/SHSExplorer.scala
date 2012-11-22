package ch.epfl.craft.view.snippet

import net.liftweb.http.StatefulSnippet
import scala.xml.NodeSeq
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Semester
import net.liftweb.json._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE.Call
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml


class SHSExplorer extends StatefulSnippet {

  def proc = DBFactory.processer

  var data = proc.readSectionPerTopicDetail(Set("SHS"),
		  						SemesterRange(Some(Semester(2012, "fall")),
    						    Some(Semester(2012, "fall"))),
    						    Set("MA1","MA3")).toList
  						 
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
  
  val json = JObject(JField("nodes", Jnodes)::JField("links", links)::Nil)

  
  def dispatch = {case "render" => render}
  
  def render = "#script" #> Script(OnLoad(Call("drawSankeyPlot",
         json,"#drawMe")))
  
}