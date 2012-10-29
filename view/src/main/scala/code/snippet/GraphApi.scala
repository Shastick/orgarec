package code.snippet

import net.liftweb.http.rest.RestHelper
import net.liftweb.http._
import js.JsCmds.SetHtml

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 15.10.12
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
object GraphApi extends RestHelper {
  serve {
    case Req("graph" :: Nil, _, GetRequest)  =>
      JsonResponse(GraphVisual.graph2Json)
    case Req("node_mouseover"::id::Nil, _, GetRequest) =>
      JavaScriptResponse(SetHtml("info-container", <h3>{GraphVisual.details2Json(id.toInt).values}</h3> ))
  }
}