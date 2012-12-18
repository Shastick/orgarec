package ch.epfl.craft.recom.storage.db
import ch.epfl.craft.recom.processing.Processer
import scala.util.DynamicVariable

/**
 * Define what our DBFactory can hold
 */
trait DBFactory {
	def store: Storage
	def processer: Processer
}

/**
 * The actual DBFactory, exposing the [[ch.epfl.craft.recom.storage.db.Storage]] and the [[ch.epfl.craft.recom.processing.Processer]]
 */
object DBFactory extends DynamicVariable[Option[DBFactory]](None) with DBFactory {
  private var default: Option[DBFactory] = None
  
  def setDefault(dbf: DBFactory) = default = Some(dbf)
  
  def get = value.getOrElse {
    val dbf = default.get
    value = Some(dbf)
    dbf
  }
  
  def store = get.store
  def processer = get.processer
}