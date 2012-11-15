package ch.epfl.craft.view.snippet
import net.liftweb.http.StatefulSnippet
import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml

class ControlPanel extends StatefulSnippet {
  import scala.collection.Set
  
  protected var sections: Set[Section] = Set.empty
  protected var years: Set[Semester] = Set.empty
  protected var levels: Set[AcademicSemester] = Set.empty
  
  def dispatch = {case "render" => render}
  
  def render = "#param-selector *" #> 
  	SHtml.multiSelectObj[Section](Seq((Section("SC"),"SC")), Seq.empty, l => l)
}