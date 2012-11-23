package ch.epfl.craft.view.snippet

import net.liftweb.http.StatefulSnippet
import scala.xml.NodeSeq
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Semester
import net.liftweb.json._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE.Call
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import ch.epfl.craft.view.snippet.details.SankeyPlot


class SHSExplorer extends StatefulSnippet {

  def proc = DBFactory.processer

  val data = proc.readSectionPerTopicDetail(Set("SHS"),
		  						SemesterRange(Some(Semester(2012, "fall")),
    						    Some(Semester(2012, "fall"))),
    						    Set("MA1","MA3")).toList

  def dispatch = {case "render" => render}
  
  def render = "#script" #> SankeyPlot.fromSectionPerTopicDetail(data).draw
}