package ch.epfl.craft.recom.processing

import java.sql.Connection
import java.sql.CallableStatement
import java.sql.Types
import java.lang.StringBuffer
import scala.collection.mutable.ListBuffer
import java.sql.ResultSet
import java.sql.Date
import ch.epfl.craft.recom.util.SemesterRange
import ch.epfl.craft.recom.model.administration.Section


/**
 * ORIGINAL AUTHOR: Tobias Schlatter -- Flizit -- Switzerland
 * 
 * Adapted for the orgarec project's purposes.
 * 
 * Allows to nicely call stored procedures with a jndi backend
 */
trait SQLCallable {
  
  /** Anything we can get results on */
  private trait RGet {
    def getLong(i: Int): Long
    def getDouble(i: Int): Double
    def getString(i: Int): String
    def getDate(i: Int): java.sql.Date
    def getBoolean(i: Int): Boolean
    def getArray(i: Int): java.sql.Array
  }
  
  /** Conversion from CallableStatement to RGet */
  private implicit def callable2rget(call: CallableStatement) = new RGet() {
    def getLong(i: Int)    = call.getLong(i)
    def getDouble(i: Int)  = call.getDouble(i)
    def getString(i: Int)  = call.getString(i)
    def getDate(i: Int)    = call.getDate(i)
    def getBoolean(i: Int) = call.getBoolean(i) 
    def getArray(i: Int)   = call.getArray(i)
  }
  
  /** Conversion from ResultSet to RGet */
  private implicit def rs2rget(call: ResultSet) = new RGet() {
    def getLong(i: Int)   = call.getLong(i)
    def getDouble(i: Int) = call.getDouble(i)
    def getString(i: Int) = call.getString(i)
    def getDate(i: Int)   = call.getDate(i)
    def getBoolean(i: Int) = call.getBoolean(i) 
    def getArray(i: Int)   = call.getArray(i)
  }
  
  /** Returns a new jndi connection */
  protected def conn: Connection
  
    /** Calls stored procedure that returns a 5-tuple list with types A,B,C,D,E,F */
    protected def callS[A : Manifest, B : Manifest, C : Manifest, D : Manifest, E : Manifest, F: Manifest]
		  			(name: String)(args: Any*): List[(A,B,C,D,E, F)] = {
    val call = conn.prepareCall("{call " + name + argstr(args :_*) + "}")
    setArgs(call, 1, args :_*)
    fetchRows(call) { rs =>
      (sqlGet[A](rs,1),sqlGet[B](rs,2), sqlGet[C](rs,3), sqlGet[D](rs,4), sqlGet[E](rs,5), sqlGet[F](rs,6))
    }
  }
  
  /** Calls stored procedure that returns a 5-tuple list with types A,B,C,D,E */
  protected def callS[A : Manifest, B : Manifest, C : Manifest, D : Manifest, E : Manifest]
		  			(name: String)(args: Any*): List[(A,B,C,D,E)] = {
    val call = conn.prepareCall("{call " + name + argstr(args :_*) + "}")
    setArgs(call, 1, args :_*)
    fetchRows(call) { rs =>
      (sqlGet[A](rs,1),sqlGet[B](rs,2), sqlGet[C](rs,3), sqlGet[D](rs,4), sqlGet[E](rs,5))
    }
  }
  
  /** Calls stored procedure that returns a 3-tuple list with types A,B,C */
  protected def callS[A : Manifest, B : Manifest, C : Manifest]
		  		    (name: String)(args: Any*): List[(A,B,C)] = {
    
    val call = conn.prepareCall("{call " + name + argstr(args :_*) + "}")
    setArgs(call, 1, args :_*)
    
    fetchRows(call) { rs =>
      (sqlGet[A](rs, 1), sqlGet[B](rs, 2), sqlGet[C](rs, 3))
    }

  }
  
  /** Calls stored procedure that returns a 2-tuple list with types A,B */
  protected def callS[A : Manifest, B : Manifest]
		  		    (name: String)(args: Any*): List[(A,B)] = {
    
    val call = conn.prepareCall("{call " + name + argstr(args :_*) + "}")
    setArgs(call, 1, args :_*)
    
    fetchRows(call) { rs =>
      (sqlGet[A](rs, 1), sqlGet[B](rs, 2))
    }

  }
  
  /** Calls stored procedure that returns list with type A */
  protected def callS[A : Manifest]
		  		    (name: String)(args: Any*): List[A] = {
    
    val call = conn.prepareCall("{call " + name + argstr(args :_*) + "}")
    setArgs(call, 1, args :_*)
    
    fetchRows(call) { rs => sqlGet[A](rs, 1) }
  }
  
  /** Calls stored procedure that returns a result with type T */
  protected def callV[T : Manifest](name: String)(args: Any*): T = {
    val call = conn.prepareCall("{? = call " + name + argstr(args :_*) + "}")
    setArgs(call, 2, args :_*)
    
    call.registerOutParameter(1, sqlType[T])
    
    call.execute()

    val res = sqlGet[T](call, 1)
    
    call.close()
    
    res
  }
  
  /** Calls a stored procedure with no return value */
  protected def call(name: String)(args: Any*) {
    val call = conn.prepareCall("{? = call " + name + argstr(args :_*) + "}")
    setArgs(call, 2, args :_*)
    call.execute()
    call.close()
  }
  
  /** Fetches rows from ResultSet, closes ResultSet */ 
  private def fetchRows[T](rs: ResultSet)(f: ResultSet => T) = {
    val lb = new ListBuffer[T]()
    
    while (rs.next()) {
      lb += f(rs)
    }
    
    rs.close()
    lb.toList
  }
  
  /** Fetches rows from CallableStatement, closes Statement */ 
  private def fetchRows[T](call: CallableStatement)(f: ResultSet => T) = {
    val rs = call.executeQuery()
    val res = fetchRows[T](rs)(f)

    call.close()
    res
  }
  
  /** Gets value with type T from RGet */
  private def sqlGet[T](call: RGet, i: Int = 1)
  					   (implicit m: Manifest[T]): T = {
         if (m.erasure.equals(classOf[Long]))    call.getLong(i)
    else if (m.erasure.equals(classOf[Int]))	 call.getLong(i).toInt
    else if (m.erasure.equals(classOf[Double]))  call.getDouble(i)
    else if (m.erasure.equals(classOf[String]))  call.getString(i)
    else if (m.erasure.equals(classOf[Date]))    new java.util.Date(call.getDate(i).getTime())
    else if (m.erasure.equals(classOf[Boolean])) call.getBoolean(i)
    else if (m.erasure.equals(classOf[List[_]])) 
      fetchRows(call.getArray(i).getResultSet()) { rs =>
        sqlGet(rs, 2)(m.typeArguments.head)
      }
    else sys.error(m.toString + " not SQLGettable")
  }.asInstanceOf[T]
  
  /** Returns a java.sql.Types type for a type T */
  private def sqlType[T](implicit m: ClassManifest[T]) = {
    if (m.erasure.equals(classOf[Long]))    Types.BIGINT    else 
    if (m.erasure.equals(classOf[Int]))		Types.INTEGER	else
    if (m.erasure.equals(classOf[Double]))  Types.DOUBLE    else
    if (m.erasure.equals(classOf[String]))  Types.CHAR      else
    if (m.erasure.equals(classOf[Date]))    Types.TIMESTAMP else
    if (m.erasure.equals(classOf[Boolean])) Types.BOOLEAN   else
    if (m.erasure.equals(classOf[List[_]])) Types.ARRAY     else
    Types.OTHER
  }
  
  /**
   * Sets arguments on a callable statement, starting at start, returns the 
   * index of the next unused argument
   */
  private def setArgs(call: CallableStatement, start: Int, args: Any*) = {
	val setter = setArg(call) _
    args.map(setter).foldLeft(start)((i,f) => f(i)) 
  }
  
  /**
   * Returns the argument string (e.g. (?,?,?)) for a function call with
   * the given arguments. Note that len(args) does not necessarily equal the
   * number of arguments, as a scala argument may expand to more than one sql
   * argument. (see arglen)
   */
  private def argstr(args: Any*) = {
    val numarg = args.map(arglen).sum
    "(" + List.fill(numarg)("?").mkString(",") + ")"
  }

  /**
   * Returns the number of sql arguments used by a scala argument
   */
  private def arglen(arg: Any) = arg match {
    case r: SemesterRange => 2
    case _ => 1
  }  
    
 /**
  * Sets an argument on a callable statement, starting at start, returns the
  * index of the next unused argument
  */
  private def setArg(st: CallableStatement)(arg: Any)(i: Int) = arg match {
  	case s: String => st.setString(i, s); i + 1
  	case r: SemesterRange => {
  	  st.setDate(i, r.from.map(s => new Date(s.year.getTime)).orNull)
  	  st.setDate(i+1, r.to.map(s => new Date(s.year.getTime)).orNull)
  	  i + 2
  	}
  	case l: Long => st.setLong(i, l); i + 1
  	case s: List[_] if s.head.isInstanceOf[Double] => {
  	  val arr =
  	    conn.createArrayOf("double", s.asInstanceOf[List[Object]].toArray)
  	  st.setArray(i, arr)
  	  i + 1
  	}
  	case s: List[_] if s.head.isInstanceOf[String] => {
  	  val arr =
  	    conn.createArrayOf("varchar", s.asInstanceOf[List[Object]].toArray)
  	  st.setArray(i,arr)
  	  i + 1
  	}
  	case s: List[Section] if s.head.isInstanceOf[Section] => {
  	  val arr =
  	    conn.createArrayOf("varchar", s.map(_.name).asInstanceOf[List[Object]].toArray)
  	  st.setArray(i,arr)
  	  i + 1
  	}
    case _ => sys.error(arg.getClass.toString + " not SQLSettable")
  }
  
}