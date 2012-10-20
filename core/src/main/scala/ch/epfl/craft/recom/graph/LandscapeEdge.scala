package ch.epfl.craft.recom.graph
import ch.epfl.craft.recom.model.Topic

class LandscapeEdge(
    from: Topic.TopicID,
    to: Topic.TopicID,
    relations: Set[TopicRelation]
    )