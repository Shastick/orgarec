package ch.epfl.craft.recom.graph

import ch.epfl.craft.recom.util.TimeRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.storage.db.Storage
import ch.epfl.craft.recom.processing.Processer
import ch.epfl.craft.recom.model.Course

/**
 * Represents the picture of the topics and what we can learn from the student's history and 
 * evaluations.
 */
class Landscape(
    timerange: TimeRange, /* From when to when we want to observe data */
    section: Option[Section], /* Do we focus on a given section ? */
    nodes: Set[LandscapeNode], 
    edges: Set[LandscapeEdge])
    
object Landscape{
  
	def build(s: Storage, p: Processer, tr: TimeRange, se: Option[Section] = None): Landscape = {
	  println("pulling data...")
	  val tl = s.readCourses(se, tr)
	  // TODO : there's a lot of optimisation to do here because of redundant stuff being pulled out of the DB.
	  println("filtering stuff...")
	  val tg = tl.map(c => 
	    		(c,p.readCoStudents(c, tr)))
	    		.groupBy(_._1.id)
	    		.map(g =>
	    		  (g._1,g._2.reduce((i,j) => (i._1,i._2 ++ i._2)))
	    		 )
	  println("merging stuff...")
	  val tsg = tg.map(m =>
	    (m._2._1.topic,m._2._2.groupBy(_._1.id)
	        .map(_._2.reduce((i,j) => (i._1, i._2 + j._2)))
	        .map(t => (t._1.topic,t._2)))
	    )
	  println("generating nodes and edges")
	    val nodes = tsg.map(t => LandscapeNode(t._1, Set.empty)).toSet
	    val edges = tsg.flatMap(t => t._2.map(u => LandscapeEdge(t._1.id, u._1.id, Set(CoStudents(u._2))))).toSet
	  
	  new Landscape(tr,se, nodes, edges)
	}
}