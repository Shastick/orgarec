package ch.epfl.craft.view.snippet

import ch.epfl.craft.recom.graph.{CoStudents, StudentsQuantity}
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.{Semester, Section}
import ch.epfl.craft.view.model.LandscapeHolder
import ch.epfl.craft.recom.model.administration._

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 04.11.12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
class StudyPlanCompleteGraph {
  val graph = LandscapeToGraph

  def getLandscape = LandscapeHolder.current

  def LandscapeToGraph:Graph = {
    val landscape = getLandscape
    val nodes= landscape.nodes.values.map(n => Node(
      n.node.id, name = n.node.name,
      radius = 4*n.node.credits.getOrElse(4),
      strokeWidthCategory = {
        val number = n.metadata.collectFirst {
          case StudentsQuantity(c) => c
        }.getOrElse(0.0)
        if(number > 100) 3
        else if (number>50) 2
        else 1
      }
    )).toList
    val links =landscape.edges.values.map(e => {
      val coStudentsN = e.relations.collectFirst {
        case CoStudents(c) => c
      }.getOrElse(0)
      Link(
        sourceID= e.from,
        targetID = e.to,
        distance =  List(0, 100-coStudentsN).max,
        coStudents = coStudentsN
    )}).toList

    println("Number of nodes computed: "+ nodes.length)
    println("Number of links computed: "+ links.length)
    Graph(nodes, links)
  }


}
