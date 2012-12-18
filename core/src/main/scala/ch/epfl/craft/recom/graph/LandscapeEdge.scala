package ch.epfl.craft.recom.graph
import ch.epfl.craft.recom.model.Topic

/**
 * Holds any relation between two topics. 
 * 
 * Landscape edges are directed.
 */
case class LandscapeEdge(
    from: Topic.TopicID,
    to: Topic.TopicID,
    relations: Set[TopicRelation]
    )