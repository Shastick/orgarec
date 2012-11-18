package ch.epfl.craft.view.view
import ch.epfl.craft.recom.model.Topic
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc.LinkText
import scala.xml.Text
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.view.model.LandscapeHolder
import net.liftweb.common.Box
import net.liftweb.http.LiftRules
import net.liftweb.common.Empty


object Site {
	/*
  val topicDetailSimple = Menu.param[String](
    "Topic Details",
	LinkText(x => Text("Details for topic %s".format(x))),
	Some(_), _.toString) / "topicdetail"
	*/
  
  val topicDetail = Menu.params[(Topic.TopicID, Landscape)](
	"Topic Details",
	LinkText(x => Text("Details for topic %s".format(x))),
	detailParamExt(_), detailParamMk(_)) / "topicdetail" 


  def detailParamExt(args: List[String]): Box[(Topic.TopicID, Landscape)] =
	if(args.size==1) LandscapeHolder.is.map(l => (args(0),l._1))
	else Empty
  
    
  def detailParamMk(arg: (Topic.TopicID, Landscape)) = {
    val (id,ls) = arg
    List(id)
  }
	

}