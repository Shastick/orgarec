package ch.epfl.craft.view.snippet
import scala.xml.NodeSeq
import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.storage.db.DBFactory
import net.liftweb.http.SHtml
import net.liftweb.http.StatefulSnippet
import net.liftweb.util.Helpers._
import ch.epfl.craft.view.model.LandscapeHolder
import ch.epfl.craft.recom.util.SemesterRange

/**
 * Panel to control the parameters used to generate the underlying landscape object.
 * When the form is submitted, the LandscapeHolder is updated
 * with the new generated landscape.
 */
class CourseExplorer extends ControlPanel {
  import scala.collection.Seq
  
  def ls = LandscapeHolder.landscape
  
  var sections: Seq[Section] = ls.sections.toSeq
  var startSem: Option[Semester] = ls.semesterRange.from
  var endSem: Option[Semester] = ls.semesterRange.to
  var levels: Seq[AcademicSemester] = ls.levels.toSeq
		  		
  def update = LandscapeHolder.build(SemesterRange(startSem, endSem),
      sections.toSet, levels.toSet)
}