package ch.epfl.craft.recom.landscape
import ch.epfl.craft.recom.storage.db.PGDBFactory
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.util.TimeRange
import ch.epfl.craft.recom.model.administration.Section

object LandscapePuller extends App {
	val dbf = new PGDBFactory("localhost","orgarec","julien","dorloter")
	val s = dbf.store
	val p = dbf.processer
	
	val l = Landscape.build(s,p,TimeRange.all, Some(Section("SC")))
	println(l)
}