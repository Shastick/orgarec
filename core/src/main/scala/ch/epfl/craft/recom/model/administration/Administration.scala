package ch.epfl.craft.recom.model.administration
import java.util.Date


/**
 * Represent a section
 */
case class Section(name: String)

/**
 * Represent whoever is responsible for a course (one or several teachers, assistants, etc)
 * Listing the assistants 'because we can' (or, can we ?)
 */
case class Head(teachers: List[Staff], assistants: List[Staff])
