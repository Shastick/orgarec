package ch.epfl.craft.view.model
import net.liftweb.http.SessionVar
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.storage.db.DBFactory

object LandscapeHolder extends SessionVar[Option[Landscape]](None) {

  def dbf = DBFactory.get
  
  def build(r: SemesterRange, sec: Set[Section], sem: Set[Semester.Identifier])
   = set(Some(Landscape.build(dbf.store, dbf.processer,r, sec, sem)))
}