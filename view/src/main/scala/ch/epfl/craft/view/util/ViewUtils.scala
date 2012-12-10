package ch.epfl.craft.view.util

import net.liftweb.json.JsonAST.JArray
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST.JField
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.graph.CoStudents
import ch.epfl.craft.recom.graph._
import ch.epfl.craft.view.snippet.draw.D3Graph
import ch.epfl.craft.view.snippet.draw.Node
import ch.epfl.craft.view.snippet.draw.Link

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
        numberOfStudents = {
	        val number = n.metadata.collectFirst {
	          case StudentsQuantity(c) => c
	        }.getOrElse(0.0)
          number.toInt
	      })
    ).toList
    
    val links = ls.edges.values.map(e => {
      val coStudentsN = e.relations.collectFirst {
        case CoStudents(c) => c
      }.getOrElse(0)
      val fromNode = nodes.find(_.id == e.from)
      val toNode = nodes.find(_.id == e.to)
      val coStudentsPercentage = if (fromNode.isDefined && toNode.isDefined)
          if(fromNode.get.numberOfStudents > toNode.get.numberOfStudents)
            100*coStudentsN/toNode.get.numberOfStudents
          else
            100*coStudentsN/fromNode.get.numberOfStudents
        else 0
      Link(e.from, e.to, Math.max(0,100-coStudentsN), coStudentsN, coStudentsPercentage)
    }).toList

    D3Graph(nodes, links)
	}
}