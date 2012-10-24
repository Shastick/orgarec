package ch.epfl.craft.recom.graph

import ch.epfl.craft.recom.util.TimeRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.storage.db.Storage
import ch.epfl.craft.recom.processing.Processer

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
	  val tl = s.readCourses(se, tr)
	  // TODO : there's a lot of optimisation to do here because of redundant stuff being pulled out of the DB.
	  val cs = tl.map(c => (c,p.readCoStudents(c, tr)))
	  
	  
	  new Landscape(TimeRange(None,None),se, Set.empty, Set.empty)
	}
}