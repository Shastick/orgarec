package ch.epfl.craft.recom.processing.graph
import ch.epfl.craft.recom.model.Topic
import ch.epfl.craft.recom.processing.TopicRelation

class LandscapeEdge(
    from: Topic.TopicID,
    to: Topic.TopicID,
    relations: Set[TopicRelation]
    )