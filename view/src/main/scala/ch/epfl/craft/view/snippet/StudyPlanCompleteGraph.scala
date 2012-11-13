package ch.epfl.craft.view.snippet

import ch.epfl.craft.recom.storage.db.PGDBFactory
import java.util.Calendar
import ch.epfl.craft.recom.graph.{CoStudents, StudentsQuantity, Landscape}
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.{Semester, Section, Fall}
import ch.epfl.craft.recom.storage.db.DBFactory

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 04.11.12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
object StudyPlanCompleteGraph extends GraphRepresentation{
  val graph = LandscapeToGraph

  def getLandscape = {
    val dbf = DBFactory.get
    val fromS = Semester(2012, "fall")
    val toS = Semester(2012, "fall")
    val l = Landscape.build(dbf.store, dbf.processer,
    						SemesterRange(Some(fromS), Some(toS)),
    						Set(Section("SC"),Section("IN")),
    						Set("MA1","MA3"))
    l
  }

  def LandscapeToGraph:Graph = {
    val landscape = getLandscape
    val nodes= landscape.nodes.values.map(n => Node(
      n.node.id, name = n.node.name,
      radius = 4*n.node.credits.getOrElse(4),
      /*fill = {
        val number = n.metadata.collectFirst {
          case StudentsQuantity(c) => c
        }.getOrElse(0.0)
        val normNumber = 255 -  List(255,(number*255/1000).round).min.toInt
        RGBColor(normNumber,0,0)
      },*/
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
      val coStudents = e.relations.collectFirst {
        case CoStudents(c) => c
      }.getOrElse(0)
      Link(
        sourceID= e.from,
        targetID = e.to,
        distance =  List(0, 100-coStudents).max
        //showLink = coStudents>10
    )}).toList.filter(_.distance <90)

    println("Number of nodes computed: "+ nodes.length)
    println("Number of links computed: "+ links.length)
    Graph(nodes, links)
  }


}
