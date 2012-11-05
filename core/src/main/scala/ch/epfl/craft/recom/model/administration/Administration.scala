package ch.epfl.craft.recom.model.administration

/**
 * Represent a section
 */
case class Section(name: String)

/**
 * Represent whoever is responsible for a course (one or several teachers, assistants, etc)
 * Listing the assistants 'because we can' (or, can we ?)
 */
case class Head(teachers: List[Staff], assistants: List[Staff])

object Head {
  def empty: Head = Head(List.empty, List.empty)
}
