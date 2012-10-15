package code.snippet

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.{JsonResponse, GetRequest, Req}

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

    case Req("details"::id::Nil, _, GetRequest) =>{
      //println("Got request")
      //<h3>{GraphVisual.details2Json(id.toInt).values}</h3>
      //XmlResponse(<h3>LOL</h3>)
      //JavaScriptResponse(SetHtml("info-container", <h3>{GraphVisual.details2Json(id.toInt).values}</h3> ))
      JsonResponse(GraphVisual.details2Json(id.toInt))
    }
  }
}