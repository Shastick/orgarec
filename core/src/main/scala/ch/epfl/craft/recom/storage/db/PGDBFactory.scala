package ch.epfl.craft.recom.storage.db
import net.liftweb.mapper.DB
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.mapper.Schemifier
import ch.epfl.craft.recom.storage.maps._
import ch.epfl.craft.recom.storage.maps.assist._
import ch.epfl.craft.recom.processing.Processer
import ch.epfl.craft.recom.processing.PGProcesser
import ch.epfl.craft.recom.processing.maps.CourseRelationMap
import ch.epfl.craft.recom.processing.maps.TopicRelationMap
import net.liftweb.mapper.MapperRules

class PGDBFactory (
		host: 	String,
		dbname:	String,
		uname:	String,
		pwd:	String
) extends DBFactory {
  
  val db = new PostgresDB(host, dbname, uname, pwd)
  
  lazy val store: Storage = new PGStorage(DefaultConnectionIdentifier, db)
  lazy val processer: Processer = new PGProcesser(DefaultConnectionIdentifier, db)
  
  DB.defineConnectionManager(DefaultConnectionIdentifier, db)
  MapperRules.createForeignKeys_? = (_) => true
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