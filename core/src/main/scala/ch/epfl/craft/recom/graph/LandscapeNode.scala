package ch.epfl.craft.recom.graph
import ch.epfl.craft.recom.model.Topic

case class LandscapeNode(
    node: Topic,
    metadata: Set[TopicMeta]
    )