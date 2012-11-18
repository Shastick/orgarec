package ch.epfl.craft.view.util

object ViewUtils {
  //TODO @ julien handle cases where several courses are considered and the ratio might get over 1
  def tupList2RatioCsv(norm: Double, tl: List[(String, Int)]) =
    if(tl.size > 0)
	    tl.map(t => 
	      t._1 + ","
	      +  (t._2/norm).toString)
	    .reduce(_ + "\n" +_)
	else ""
}