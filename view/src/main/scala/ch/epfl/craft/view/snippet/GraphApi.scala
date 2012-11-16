package ch.epfl.craft.view.snippet

import ch.epfl.craft.recom.util.SemesterRange
import net.liftweb.http.rest.RestHelper
import net.liftweb.http._
import js.JsCmds.SetHtml
import org.scala_tools.time.Imports._
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.model.administration.Section

object GraphApi extends RestHelper {
    
  serve {
    case "graph" :: _ JsonGet _ =>
      JsonResponse(GraphVisual.graph2Json)
    case "node_mouseover" :: id :: _ JsonGet _ =>
      JavaScriptResponse(SetHtml("detail-data", <h3>{GraphVisual.details2Json(id).values}</h3> ))
    case "context_menu" :: id :: _ JsonGet _ =>
      JavaScriptResponse(SetHtml("context_menu", GraphVisual.contextMenuContent(id) ))
  }
}