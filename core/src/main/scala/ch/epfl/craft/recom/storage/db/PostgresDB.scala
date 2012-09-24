package ch.epfl.craft.recom.storage.db
import net.liftweb.db.ConnectionManager
import net.liftweb.db.ConnectionIdentifier
import net.liftweb.common.Full
import net.liftweb.common.Empty
import java.sql.DriverManager
import java.sql.Connection

class PostgresDB(host: String, db: String, usr: String, pwd: String)
extends ConnectionManager {
	Class.forName("org.postgresql.Driver")
	
	def newConnection(name: ConnectionIdentifier) = {
	  try {
	    Full(DriverManager.getConnection(
	        "jdbc:postgresql://"+host+"/"+db, usr, pwd))
	  } catch {
	    case e : Exception => e.printStackTrace()
	    					Empty
	  }
	}
	
	def releaseConnection(c: Connection) = c.close
}