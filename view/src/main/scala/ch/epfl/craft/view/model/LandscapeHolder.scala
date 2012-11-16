package ch.epfl.craft.view.model
import net.liftweb.http.SessionVar
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.model.administration._

object LandscapeHolder extends SessionVar[Option[Landscape]](None) {

  def dbf = DBFactory.get
  
  def default = Landscape.build(dbf.store, dbf.processer,
    						SemesterRange(Some(Semester(2012, "fall")),
    						    Some(Semester(2012, "fall"))),
    						Set(Section("SC"),Section("IN")),
    						Set(MA1(None),MA3(None)))

  //TODO : store defaults in a place making more sense ?
  def current = is.getOrElse{
    performAtomicOperation(set(Some(default)))
    is.get
  }
  
  def build(r: SemesterRange, sec: Set[Section], sem: Set[AcademicSemester]) = 
  	performAtomicOperation {
    val l = Landscape.build(dbf.store, dbf.processer,r, sec, sem)
    set(Some(l))
    println("Landscape replaced.")
    l
  }
}