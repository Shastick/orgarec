package ch.epfl.craft.recom.storage
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.HasManyThrough
import net.liftweb.mapper.MappedString
import ch.epfl.craft.recom.storage.assist.Prerequisite
import ch.epfl.craft.recom.model.Topic
import net.liftweb.mapper.By
import ch.epfl.craft.recom.model.administration.Section

class TopicMap extends LongKeyedMapper[TopicMap] with IdPK {
	def getSingleton = TopicMap
	
	val name_len = 200
	val isa_id_len = 20
	val descr_len = 10000
	
	object isa_id extends MappedString(this, isa_id_len)
	object name extends MappedString(this, name_len)
	object section extends MappedLongForeignKey(this,SectionMap)
	object description extends MappedString(this, descr_len)

	// Prerequisites
	object prerequisites extends HasManyThrough(this, TopicMap, Prerequisite,
	    Prerequisite.required,Prerequisite.topic)
}

object TopicMap extends TopicMap with LongKeyedMetaMapper[TopicMap] {
  
  /**
   * Fill a topic and save it to DB WITHOUT setting the prerequisites !
   */
  
  def fill(t: Topic): TopicMap = {
    val tm = TopicMap.findOrCreate(By(TopicMap.isa_id,t.id))
    	tm.isa_id(t.id)
    	  .name(t.name)
    	  .section(SectionMap.fill(t.section))
    t.description.foreach(tm.description(_))
    
    tm.save
    tm
  }
  /**
   * bindFill actually populates the prerequisite table. It should only be called 
   * once the topics have been inserted, as the relation of a topic is on itself.
   */
  def bindFill(t: Topic): TopicMap = {
    	val tm = fill(t)
    	Prerequisite.setPrerequisites(t, tm)
    	tm	
  }
  
  def fill(t: TopicMap): Topic = {
    val section = t.section.map{SectionMap.fill(_)}.getOrElse(throw new Exception("Undefined section."))
    val prereqs_id = t.prerequisites.get.map(_.isa_id.get).toSet
    new Topic(t.isa_id, t.name, section, prereqs_id, Option(t.description))
  }
  
  def read(tid: Topic.TopicID):Option[Topic] = TopicMap.find(By(TopicMap.isa_id,tid)).map(fill _)

}