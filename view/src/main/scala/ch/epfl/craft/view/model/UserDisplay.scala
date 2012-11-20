package ch.epfl.craft.view.model
import net.liftweb.http.SessionVar

/**
 * Session variable holding what nodes the user has on his screen.
 */
object UserDisplay extends SessionVar[(List[Node], List[Link])](List.empty, List.empty) {
	
	def nodes = is._1
	def links = is._2
	
	def reset(nl: List[Node], ll: List[Link]) = performAtomicOperation(set(nl,ll))
	
	def setLinks(ll: List[Link]) = performAtomicOperation(set(nodes, ll))
	def setNodes(nl: List[Node]) = performAtomicOperation(set(nl,links))
	
	def addLinks(ll: List[Link]) = performAtomicOperation(set(nodes,links ++ ll))
	def addNodes(nl: List[Node]) = performAtomicOperation(set(nodes ++ nl, links))
	
	def removeLinks(ll: List[Link]) = performAtomicOperation(set(nodes,links.diff(ll)))
	def removeNodes(nl: List[Node]) = performAtomicOperation(set(nodes.diff(nl),links))
	
	def render = D3Graph(nodes,links).toJson
}