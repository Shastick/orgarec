package ch.epfl.craft.recom.storage.db
import net.liftweb.mapper.DB
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.mapper.Schemifier
import ch.epfl.craft.recom.storage._
import ch.epfl.craft.recom.storage.assist._

class PGDBFactory (
		host: 	String,
		dbname:		String,
		uname:	String,
		pwd:	String
) {
  val db = new PostgresDB(host, dbname, uname, pwd)

  DB.defineConnectionManager(DefaultConnectionIdentifier, db)

  Schemifier.schemify(	true,
		  				Schemifier.infoF _,
		  				CourseMap,
		  				SectionMap,
		  				SemesterMap,
		  				StaffMap,
		  				StudentMap,
		  				TopicMap,
		  				Assists,
		  				Prerequisite,
		  				Subscribed,
		  				Teaches)
}