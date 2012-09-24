package code
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> date.map(_.toString)


  def showGraph = {
    <head>
      <!--<script type="text/javascript" src="static/scripts/Raphael/raphael-min.js"></script>
      <script type="text/javascript" src="static/scripts/raphaelTest.js"></script>     -->
      <!--<style type="text/css">
        #canvas_container {
        width: 500px;
        border: 1px solid #aaa;
      }
      </style>-->
      <script type="text/javascript" src="static/scripts/d3/d3.v2.js"></script>
      <link rel="stylesheet" type="text/css" href="static/css/d3-test.css" />
    </head> ++
    <div id="graph-container" width="500px" height="100px">
      <script type="text/javascript" src="static/scripts/d3Test.js"></script>
    </div>  ++
    <div>
      <h3>SVG element</h3>
      <svg width="500" height="50" xmlns="http://www.w3.org/2000/svg" version="1.1">
        <rect x="0" y="0" width="500" height="50"/>
      </svg>
    </div>

  }
  /*
   lazy val date: Date = DependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> date.toString
   */
}

