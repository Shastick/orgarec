package ch.epfl.craft.view.snippet
import scala.xml.NodeSeq

import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.storage.db.DBFactory
import net.liftweb.http.SHtml
import net.liftweb.http.StatefulSnippet
import net.liftweb.util.Helpers._

class ControlPanel extends StatefulSnippet {
  import scala.collection.Set
  
  protected var sections: Seq[Section] = Seq.empty
  protected var semesters: Seq[Semester] = Seq.empty
  protected var levels: Seq[AcademicSemester] = Seq.empty
  
  val store = DBFactory.store
  
  def dispatch = {case "render" => render}
  
  def render =	"#section-choice *" #> sectionChoice _ &
		  		"#semester-choice *" #> semesterChoice _ &
		  		"#level-choice *" #> levelChoice _
  	
  def sectionChoice(n: NodeSeq) = {
    val secSeq = store.readAllSections.map(s => (s, s.name)).toSeq
    def upd(l: Seq[Section]) = {sections = l}
    SHtml.multiSelectObj[Section](secSeq, sections, upd _)
  }
		  		
  def semesterChoice(n: NodeSeq) =  {
    val semSeq = store.readAllSemesters
    				.map(s => (s, s.year_int.toString + "-" + s.season))
    				.toSeq
    def upd(l: Seq[Semester]) = {semesters = l}
    SHtml.multiSelectObj[Semester](semSeq, semesters, upd _)
  }
 
  def levelChoice(n: NodeSeq) = {
	val lvlSeq = store.readAllAcademicLevels.map(l => (l,l.level)).toSeq
	def upd(l: Seq[AcademicSemester]) = {levels = l}
    SHtml.multiSelectObj[AcademicSemester](lvlSeq, levels, upd _)
  }
}