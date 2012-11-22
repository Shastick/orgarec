package ch.epfl.craft.view.snippet.details

import net.liftweb.http.js.JE.Call
import net.liftweb.http.js.JsCmds.OnLoad
import net.liftweb.http.js.JsCmds.Script
import net.liftweb.http.js.JsCmds.jsExpToJsCmd
import scala.xml.NodeSeq

class BarPlot(csv: String, elemSelector: String, w: Int, h: Int) {
  
  lazy val draw = Script(OnLoad(Call("drawBarPlot2",
         "name,ratio\n"+csv,
    	elemSelector,w,h)))
}

object BarPlot {
  
  val includes: NodeSeq = {
        <script language="javascript" type="text/javascript"
		    src="/static/scripts/jquery-1.8.3.min.js"></script> ++
        <script language="javascript" type="text/javascript"
		    src="/static/scripts/d3/d3.v2.min.js"></script>  ++
        <script language="javascript" type="text/javascript"
		  	src="/static/scripts/barPlotter.js"></script>
  }
  
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