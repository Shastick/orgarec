package ch.epfl.craft.view.snippet

import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.view.snippet.details.SankeyPlot
import net.liftweb.util.Helpers.strToCssBindPromoter
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.AcademicSemester


class SectionExplorer extends ControlPanel {

  var sections: Seq[Section] = Seq(Section("SHS"))
  var startSem: Option[Semester] = Some(Semester(2012, "fall"))
  var endSem: Option[Semester] = Some(Semester(2012, "fall"))
  var levels: Seq[AcademicSemester] = Seq(AcademicSemester("MA1"),AcademicSemester("MA2"))
  
  var data = extract

  override def dispatch = {
    case "render" => subRender
  	case "shs" => shs
  }
  
  lazy val shs = "#script" #> SankeyPlot.fromSectionPerTopicDetail(data).draw
  
  def subRender = {
    render &
    refresh
  }
  
  def update = {data = extract}
  
  def refresh = "#script" #> SankeyPlot.fromSectionPerTopicDetail(data).draw
    
  private def extract = proc.readSectionPerTopicDetail(sections.map(_.name).toSet,
		  						range,levels.map(_.level).toSet).toList
}