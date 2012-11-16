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

class ControlPanel extends StatefulSnippet {
  import scala.collection.Seq
  
  val ls = LandscapeHolder.is
  
  protected var sections: Seq[Section] =
    ls.map(_.sections.toSeq).getOrElse(Seq.empty)
  protected var startSem: Option[Semester] =
    ls.flatMap(l => l.semesterRange.from)
  protected var endSem: Option[Semester] =
    ls.flatMap(l => l.semesterRange.to)
  protected var levels: Seq[AcademicSemester] =
    ls.map(_.levels.toSeq).getOrElse(Seq.empty)
  
  lazy val semSeq = store.readAllSemesters
    				.map(s => (s, s.year_int.toString + "-" + s.season))
    				.toSeq
    				
  val store = DBFactory.store
  
  def dispatch = {case "render" => render}
  
  def render =	"#section-choice *" #> sectionChoice _ &
		  		"#start-semester *" #> startSemester _ &
		  		"#end-semester *" #> endSemester _ &
		  		"#level-choice *" #> levelChoice _ &
		  		"#update-graph" #> SHtml.onSubmitUnit(refresh)
  	
		  		
  def refresh() = LandscapeHolder.build(SemesterRange(startSem, endSem),
      sections.toSet, levels.toSet)
  
  def sectionChoice(n: NodeSeq) = {
    val secSeq = store.readAllSections.map(s => (s, s.name)).toSeq
    def upd(l: Seq[Section]) = {sections = l}
    SHtml.multiSelectObj[Section](secSeq, sections, upd _)
  }
		  		
  def startSemester(n: NodeSeq) =  
    SHtml.selectObj[Semester](semSeq, startSem, s => startSem = Some(s))
  
  
  def endSemester(n: NodeSeq) = 
    SHtml.selectObj[Semester](semSeq, startSem, s => endSem = Some(s))
  
 
  def levelChoice(n: NodeSeq) = {
	val lvlSeq = store.readAllAcademicLevels.map(l => (l,l.level)).toSeq
	def upd(l: Seq[AcademicSemester]) = {levels = l}
    SHtml.multiSelectObj[AcademicSemester](lvlSeq, levels, upd _)
  }
}