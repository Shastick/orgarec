package ch.epfl.craft.recom.graph
import ch.epfl.craft.recom.model.Topic

/**
 * Holds any information relative to a single topic in the landscape.
 */
case class LandscapeNode(
    node: Topic,
    metadata: Set[TopicMeta]
    )