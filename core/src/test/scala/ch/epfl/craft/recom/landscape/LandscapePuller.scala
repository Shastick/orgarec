package ch.epfl.craft.recom.landscape
import ch.epfl.craft.recom.storage.db.PGDBFactory
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import org.scala_tools.time.Imports._
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.storage.maps.SemesterMap

object LandscapePuller extends App {
	val dbf = new PGDBFactory("localhost","orgarec","julien","dorloter")
	val s = dbf.store
	val p = dbf.processer
	val now = new java.util.Date()
	
	val from = Semester("2012","fall")
	val to = Semester("2012","fall")
	
	val l = Landscape.build(s,p,SemesterRange(Some(from),Some(to)), Set(Section("SC"),Section("IN")))
	val then = new java.util.Date()
	
	println("Pull time: " + (then.getTime() - now.getTime()).toString())
	println(l.nodes.size)
	println(new java.sql.Date(to.year.getMillis()))
	//l.nodes.foreach(n => println(n.node.name + " " + n.node.credits))
	//l.edges.foreach(println _)
	
}