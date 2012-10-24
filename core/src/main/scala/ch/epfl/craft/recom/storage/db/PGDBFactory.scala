package ch.epfl.craft.recom.storage.db
import net.liftweb.mapper.DB
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.mapper.Schemifier
import ch.epfl.craft.recom.storage.maps._
import ch.epfl.craft.recom.storage.maps.assist._
import ch.epfl.craft.recom.processing.Processer
import ch.epfl.craft.recom.processing.SQLProcesser
import ch.epfl.craft.recom.processing.maps.CourseRelationMap
import ch.epfl.craft.recom.processing.maps.TopicRelationMap

class PGDBFactory (
		host: 	String,
		dbname:	String,
		uname:	String,
		pwd:	String
) {
  
  val db = new PostgresDB(host, dbname, uname, pwd)
  
  lazy val store: Storage = new PGStorage(DefaultConnectionIdentifier, db)
  lazy val processer: Processer = new SQLProcesser(DefaultConnectionIdentifier, db)
  
  DB.defineConnectionManager(DefaultConnectionIdentifier, db)

  Schemifier.schemify(	true,
		  				Schemifier.infoF _,
		  				CourseMap,
		  				SectionMap,
		  				SemesterMap,
		  				AcademicSemesterMap,
		  				StaffMap,
		  				StudentMap,
		  				TopicMap,
		  				Assists,
		  				Prerequisite,
		  				Subscribed,
		  				Teaches,
		  				CourseRelationMap,
		  				TopicRelationMap)
}