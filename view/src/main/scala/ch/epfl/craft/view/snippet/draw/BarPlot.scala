package ch.epfl.craft.view.snippet.draw

import net.liftweb.http.js.JE.Call
import net.liftweb.http.js.JsCmds.OnLoad
import net.liftweb.http.js.JsCmds.Script
import net.liftweb.http.js.JsCmds.jsExpToJsCmd
import scala.xml.NodeSeq

class BarPlot(csv: String, elemSelector: String, w: Int, h: Int) {
  
  def draw = Script(Call("drawBarPlot",
         "name,ratio\n"+csv,
    	elemSelector,w,h))

}

object BarPlot {
  
  def ratioFromDoubleTup(norm: Double,
      tl: List[(String, Double)],
      sel: String,
      w: Int,
      h: Int): BarPlot = {
    val csv =   if(tl.size > 0)
		    tl.map(t => 
		      (t._1 match{
		        case "" => "_"
		        case s: String => s
		      }) + "," +  scala.math.min(1.0,t._2/norm).toString)
		    .reduce(_ + "\n" +_)
		    else ""
	new BarPlot(csv, sel, w, h)
  }
  
  def ratioFromIntTup(norm: Double,
      tl: List[(String, Int)],
      sel: String,
      w: Int,
      h: Int): BarPlot = {
	val csv = if(tl.size > 0)
			    tl.map(t => 
			      (t._1 match{
			        case "" => "_"
			        case s: String => s
			      }) + "," +  scala.math.min(1.0,t._2/norm).toString)
			    .reduce(_ + "\n" +_)
			else ""
	new BarPlot(csv, sel, w, h)
  }
}