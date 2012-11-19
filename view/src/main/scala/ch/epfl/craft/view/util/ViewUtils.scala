package ch.epfl.craft.view.util

import net.liftweb.json.JsonAST.JArray
import net.liftweb.json.JsonAST.JObject
import ch.epfl.craft.view.model.D3Graph
import net.liftweb.json.JsonAST.JField
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.graph.CoStudents
import ch.epfl.craft.view.model.Node
import ch.epfl.craft.view.model.Link
import ch.epfl.craft.recom.graph._

object ViewUtils {

  def tupListDouble2RatioCsv(norm: Double, tl: List[(String, Double)]): String =
    if(tl.size > 0)
	    tl.map(t => 
	      (t._1 match{
	        case "" => "_"
	        case s: String => s
	      }) + "," +  Math.min(1.0,t._2/norm).toString)
	    .reduce(_ + "\n" +_)
	else ""
  
  def tupListInt2RatioCsv(norm: Double, tl: List[(String, Int)]): String =
    if(tl.size > 0)
	    tl.map(t => 
	      (t._1 match{
	        case "" => "_"
	        case s: String => s
	      }) + "," +  Math.min(1.0,t._2/norm).toString)
	    .reduce(_ + "\n" +_)
	else ""

  def LandscapeToD3Graph(ls: Landscape) = {
	  val nodes = ls.nodes.values.map(n =>
      Node(n.node.id,n.node.name, 4*n.node.credits.getOrElse(4),
	      strokeWidthCategory = {
	        val number = n.metadata.collectFirst {
	          case StudentsQuantity(c) => c
	        }.getOrElse(0.0)
	        if(number > 100) 3
	        else if (number>50) 2
	        else 1
	      })
    ).toList
    
    val links = ls.edges.values.map(e => {
      val coStudentsN = e.relations.collectFirst {
        case CoStudents(c) => c
      }.getOrElse(0)
      Link(e.from, e.to, Math.max(0,100-coStudentsN), coStudentsN)
    }).toList

    D3Graph(nodes, links)
	}
}