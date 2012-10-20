package ch.epfl.craft.recom.processing.graph

import ch.epfl.craft.recom.util.TimeRange
import ch.epfl.craft.recom.model.administration.Section

/**
 * Represents the picture of the topics and what we can learn from the student's history and 
 * evaluations.
 */
class Landscape(
    timerange: TimeRange, /* From when to when we want to observe data */
    section: Option[Section], /* Do we focus on a given section ? */
    nodes: Set[TopicMeta],
    edges: Set[TopicRelation])
    
object Landscape{
  
	def build(tr: TimeRange, s: Option[Section] = None): Landscape = {
	  new Landscape(TimeRange(None,None),None, Set.empty, Set.empty)
	}
}