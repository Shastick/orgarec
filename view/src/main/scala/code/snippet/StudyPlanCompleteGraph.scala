package code.snippet

import ch.epfl.craft.recom.storage.db.PGDBFactory
import java.util.Calendar
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.{Section, Fall}

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
    val dbf = new PGDBFactory("localhost", "orgarec", "postgres", "hendrix")
    val s = dbf.store
    val p = dbf.processer
    val from = {val c = Calendar.getInstance(); c.set(Calendar.YEAR, 2011); c.getTime}
    val to = {val c = Calendar.getInstance(); c.set(Calendar.YEAR, 2012); c.getTime}

    val l = Landscape.build(s, p, SemesterRange(Some(Fall(from)), Some(Fall(to))), Set(Section("SC")))
    l
  }

  def LandscapeToGraph:Graph = {
    val landscape = getLandscape
    val nodes= landscape.nodes.map(n => Node(n.node.id, n.node.name, n.node.name, n.node.credits.getOrElse(4))).toList
    val links =landscape.edges.map(e => Link(e.from, e.to, 5)).toList

    println("Number of nodes computed: "+ nodes.length)
    println("Number of links computed: "+ links.length)
    println("Node1 : "+ nodes.head.toJObject.toString)
    println("link1 : "+ links.head.toJObject.toString)
    Graph(nodes, links)
  }


}
