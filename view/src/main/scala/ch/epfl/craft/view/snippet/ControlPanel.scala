package ch.epfl.craft.view.snippet

import net.liftweb.http.StatefulSnippet
import net.liftweb.http.SHtml
import net.liftweb.http.StatefulSnippet
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.util.SemesterRange

trait ControlPanel extends StatefulSnippet {

  def store = DBFactory.store
  def proc = DBFactory.processer
  
  var sections: Seq[Section]
  var startSem: Option[Semester]
  var endSem: Option[Semester]
  var levels: Seq[AcademicSemester]
  
  def range = SemesterRange(startSem,endSem)
  
  def dispatch = {case "render" => render}
  
  def render =
    "#section-choice *" #> sectionChoice _ &
		"#start-semester *" #> startSemester _ &
		"#end-semester *" #> endSemester _ &
		"#level-choice *" #> levelChoice _ &
		"#update-graph" #> SHtml.onSubmitUnit(update)

  def update(): Any
		
  def sectionChoice(n: NodeSeq) = {
    val secSeq = store.readAllSections.map(s => (s, s.name)).toSeq.sortBy(_._2)
    def upd(l: Seq[Section]) = {sections = l}
    SHtml.multiSelectObj[Section](secSeq, sections, upd _)
  }
  
  lazy val semSeq = store.readAllSemesters
    				.map(s => (s, s.year_int.toString + "-" + s.season))
    				.toSeq.sortBy(_._2)
		  		
  def startSemester(n: NodeSeq) =  
    SHtml.selectObj[Semester](semSeq, startSem, s => startSem = Some(s))
  
  def endSemester(n: NodeSeq) = 
    SHtml.selectObj[Semester](semSeq, startSem, s => endSem = Some(s))
  
  def levelChoice(n: NodeSeq) = {
	val lvlSeq = store.readAllAcademicLevels.map(l => (l,l.level)).toSeq.sortBy(_._2)
	def upd(l: Seq[AcademicSemester]) = {levels = l}
    SHtml.multiSelectObj[AcademicSemester](lvlSeq, levels, upd _)
  }
}