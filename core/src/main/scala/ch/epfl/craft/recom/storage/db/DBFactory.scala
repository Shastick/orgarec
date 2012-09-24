package ch.epfl.craft.recom.storage.db
import net.liftweb.mapper.DB
import net.liftweb.db.DefaultConnectionIdentifier

class PGDBFactory (
		host: 	String,
		dbname:		String,
		uname:	String,
		pwd:	String
) {
  val db = new PostgresDB(host, dbname, uname, pwd)
  DB.defineConnectionManager(DefaultConnectionIdentifier, db)
  
}