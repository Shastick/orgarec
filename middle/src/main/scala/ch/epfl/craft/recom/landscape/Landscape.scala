package ch.epfl.craft.recom.landscape

/**
 * Represents the picture of the topics and what we can learn from the student's history and 
 * evaluations.
 */
trait Landscape {
	def coStudents
	def socialGraph
	def semanticDistance
	def prerequisites
}