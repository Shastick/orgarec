package ch.epfl.craft.view.util
import net.liftweb.json.JsonAST.JArray
import net.liftweb.json.JsonAST.JObject
import ch.epfl.craft.view.model.D3Graph
import net.liftweb.json.JsonAST.JField

object ViewUtils {

  def tupList2RatioCsv(norm: Double, tl: List[(String, Int)]) =
    if(tl.size > 0)
	    tl.map(t => 
	      t._1 + "," +  Math.min(1.0,t._2/norm).toString)
	    .reduce(_ + "\n" +_)
	else ""
}