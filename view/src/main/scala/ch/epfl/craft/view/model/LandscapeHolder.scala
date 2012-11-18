package ch.epfl.craft.view.model
import net.liftweb.http.SessionVar
import ch.epfl.craft.recom.graph.Landscape
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section
import ch.epfl.craft.recom.model.administration.Semester
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.model.administration.AcademicSemester
import ch.epfl.craft.recom.model.administration._
import ch.epfl.craft.recom.graph._

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
  
  def getJsGraph(): D3Graph = {
    val ls = current
    val nodes = ls.nodes.values.map(n =>
      Node(n.node.id,n.node.name, 4*n.node.credits.getOrElse(4),
	      strokeWidthCategory = {
	        val number = n.metadata.collectFirst {
	          case StudentsQuantity(c) => c
	        }.getOrElse(0.0)
	        if(number > 100) 3
	        else if (number>50) 2
	        else 1
	      })
    ).toList
    
    val links = ls.edges.values.map(e => {
      val coStudentsN = e.relations.collectFirst {
        case CoStudents(c) => c
      }.getOrElse(0)
      Link(e.from, e.to, Math.max(0,100-coStudentsN), coStudentsN)
    }).toList

    D3Graph(nodes, links)
  }
}