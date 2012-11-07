package ch.epfl.craft.view.snippet

import net.liftweb._
import common.Full
import http._
import http.js.JE.{AnonFunc, JsRaw}
import http.S.SFuncHolder
import json.JsonAST.{JString, JArray}
import mapper.DB
import xml.NodeSeq
import http.js.JsCmd
import http.js.JsCmds._
import util.Helpers._
import java.util.Calendar


/**
 * This trait is an helper to create fields in forms
 */
sealed trait SearchField {

  /** Name of the field, wil be displayed on the HTML page */
  def name: String

  /** Table corresponding to the Field in the Database */
  def table: String

  /** Name of the field in the Database*/
  def field: String

  /** Get the value of the `RequestVar` */
  def getValue: Option[Any]

  /** XML displayed for the form element corresponding to the field */
  def xml:NodeSeq
  /** SQL request corresponding to the field, it is full if the request is necessary.
   *  The first element of the tupple is the request, the second element is the `List` of parameters
   *
   *  @param attr The attribute necessary for the request (the alias of the table corresponding to the field)
   *  @return Tupple containing request
   */
  def sqlRequest(attr:String):Option[(String, List[String])]

  /** Set the value of the `RequestVar` */
  def setValue(value:Any)

  /** Resets the value of the RequestVar
   *
   * Warning: It doesn't reset the value of the field in Ajax, you have to re-load the page
   */
  def resetValue:Unit
}

/**
 * Select generator
 *
 */
case class Select(
                   override val name:String,
                   override val table:String,
                   override val field:String,
                   onBooleanVal: Boolean = false) extends SearchField {

  private val init = "all";

  /** `RequestVar` to store the `String` corresponding to the selection */
  private object cell extends RequestVar(init) {
    override def __nameSalt = "select_reqvar_cell_" + table + field
  }

  def setValue(value:Any){cell.set(value.toString)}

  def getValue = {
    if(cell.get != "all") Some(cell.get) else None
  }

  def resetValue = cell.set(init);

  /** Function to dynamicaly find the parameters of the select */
  def fieldsFound = DB.runQuery("SELECT DISTINCT " + field + " FROM " + table + " ORDER BY " + table + "." + field + " ASC")

  /** Parameters of the select */
  //def fields = ("all", "All")::fieldsFound._2.map(x=> (x.head,x.head))
  def fields = if (onBooleanVal) {
    ("all", "All")::fieldsFound._2.map(x=> (x.head, convertBooleanToYesNo(x.head)))
  } else {
    ("all", "All")::fieldsFound._2.map(x=> (x.head,x.head))
  }

  def xml = SHtml.ajaxSelect(fields, Full(cell.get), s=>{cell.set(s); Noop})

  def sqlRequest(attr:String) = {
    val attr1 = if (attr=="") "" else attr+"."
    val searchVal = if (onBooleanVal) convertBooleanToInt(cell.get).toString else cell.get
    if(cell.get!= "all") Some((attr1+field+"= ?", searchVal::Nil))
    else None
  }

  private def convertBooleanToInt(b: String): Int = if (b == "true") 1 else 0

  private def convertBooleanToYesNo(b: String): String = if (b == "true") "Yes" else "No"
}

/**
 * Date Roller: Select a date between two dates using year and month
 * Possibility to add the day if it becomes necessary but not yet implemented
 * format of `cell`: (Calendar day, Calendar month, Calendar year)
 *
 * Warning: the select for days go to 31 for every months (like february...)
 *
 * @param showDays set to true if days necessary
 */

case class DateRoller( val name:String, val table:String, val field:String, val showDays:Boolean=false) extends SearchField {
  /** Initialization of `cell`*/
  def init = (0, Calendar.UNDECIMBER, 0)

  /** `RequestVar` containing the date selected: (Calendar day, Calendar month, Calendar year)*/
  private object cell extends RequestVar(init) with Serializable {
    override def __nameSalt = "select_reqvar_cell_" + table + field
  }

  def getValue = if(cell.get!= init) Some(cell.get) else None
  def setValue(value:Any){ cell.set(value.asInstanceOf[(Int, Int, Int)])}
  def resetValue = cell.set(init);

  /** Minimum year, initialized dynamically */
  private def min = DB.runQuery("SELECT Year(MIN("+field+")) FROM "+table)._2.head.head.toInt

  /** Maximum year, initialized dynamically */
  private def max = DB.runQuery("SELECT Year(MAX("+field+")) FROM "+table)._2.head.head.toInt

  def sqlRequest(attr:String) = {
    val attr1 = if (attr.isEmpty) "" else attr+"."
    cell.get match{
      // No argument set
      case (0, Calendar.UNDECIMBER, 0) => None
      // One argument set
      case (0, Calendar.UNDECIMBER, y) => Some(("YEAR("+attr1+field+")=?",y.toString::Nil))
      case (d, Calendar.UNDECIMBER, 0) => Some(("DAY("+attr1+field+")=?",d.toString::Nil))
      case (0, m, 0) =>  Some(("MONTH("+attr1+field+")=?",(m+1).toString::Nil))
      // Two arguments set
      case (0, m, y) =>  Some(("MONTH("+attr1+field+")=?" + " AND YEAR("+attr1+field+")=?" ,(m+1).toString::y.toString::Nil))
      case (d, m, 0) =>  Some(("MONTH("+attr1+field+")=?" + " AND DAY("+attr1+field+")=?" ,(m+1).toString::d.toString::Nil))
      case (d, Calendar.UNDECIMBER, y) =>  Some(("YEAR("+attr1+field+")=?" + " AND DAY("+attr1+field+")=?" ,y.toString::d.toString::Nil))
      // All arguments set
      case (d, m, y) =>  Some(("MONTH("+attr1+field+")=? AND YEAR("+attr1+field+")=? AND DAY("+attr1+field+")=?" ,(m+1).toString::y.toString::d.toString::Nil))
    }
  }

  /** Parameters for the optional select corresponding to days*/
  private val days = ("0","All")::(1 to 31).toList.map(x=> (x.toString, x.toString));

  /** All names of months, fields for the select */
  private val monthNames = List("All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

  /** ALl values of months */
  private val monthValues = List(Calendar.UNDECIMBER, Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER)

  /** Parameters for the select corresponding to months*/
  private val months:List[(String, String)] = monthValues.map(_.toString).zip(monthNames)

  /** Parameters for the select corresponding to years*/
  private def years = ("0","All")::(min to max).toList.map(x=> (x.toString, x.toString));

  /** Select element corresponding to days */
  private def daySelector  = SHtml.ajaxSelect(days, Full(cell.get._1.toString),  s =>{val year = cell.get._3; val month = cell.get._2; cell.set(s.toInt, month, year); Noop })

  /** Select element corresponding to months */
  private def monthSelector = SHtml.ajaxSelect(months, Full(cell.get._2.toString), s =>{val year = cell.get._3;val day=cell.get._1; cell.set(day, s.toInt, year); Noop })

  /** Select element corresponding to years */
  private def yearSelector  = SHtml.ajaxSelect(years, Full(cell.get._3.toString),  s =>{val day = cell.get._1; val month = cell.get._2; cell.set(day, month, s.toInt); Noop })

  def xml =
    if(showDays)
      daySelector ++ monthSelector ++ yearSelector
    else
      monthSelector ++ yearSelector
}


/**
 * CheckBox generator
 *
 * Warning: it is better to use a select with "true" and "false" because one can then choose the "all" option
 *
 * @param initial the initial value for the checkbox (true if ticked, false otherwise)
 */
case class CheckBox(val name:String, val table:String, val field:String, initial:Boolean=true) extends SearchField {
  /** `RequestVar` containing the boolean selected */
  private object cell extends RequestVar(initial) with Serializable {
    override def __nameSalt = "select_reqvar_cell_" + table + field
  }

  def getValue = if(cell.get!= initial) Some(cell.get) else None
  def setValue(value:Any){cell.set(value.asInstanceOf[Boolean])}
  def resetValue = cell.set(initial)

  def sqlRequest(attr:String) = {
    val searchVal= if (cell.get) "1" else "0"
    val attr1 = if (attr=="") "" else attr+"."
    Some((attr1+field+"=?", searchVal::Nil))
  }

  def xml = SHtml.ajaxCheckbox(cell.get, {s=> cell.set(s); Noop})
}

/**
 * Basic text field
 *
 * @param size The size of the input text field.
 */
case class  BasicText(val name:String, val table:String, val field:String, size:Int=20) extends SearchField {

  /** `RequestVar` containing the text selected */
  private object cell extends RequestVar("") with Serializable {
    override def __nameSalt = "select_reqvar_cell_" + table + field
  }

  def getValue = if(cell.get!= "") Some(cell.get) else None
  def setValue(value:Any){cell.set(value.toString)}
  def resetValue = cell.set("");

  def sqlRequest(attr:String) = {
    val attr1 = if (attr=="") "" else attr+"."
    if (cell.get.nonEmpty) Some(("UPPER("+attr1+field+") LIKE ?", ("%"+cell.get.toUpperCase+"%")::Nil))
    else None
  }

  def xml = SHtml.ajaxText(cell.get, s=>{ cell.set(s); Noop}, "size" ->  size.toString)
}

/**
 * Autocomplete text field
 *
 * @param limit The maximum number of suggestions
 * @param condition set an additional condition for the autocomplete. This can be useful for ManyToMany relations
 *                              the first element is the tab where to apply the condition (FROM ...), the second one is the actual
 *                              condition (WHERE ...)
 * @param size The size of the input text field
 *
 */
case class AutoCompleteText(val name:String, val table:String, val field:String, limit:Int=5, val size:Int=20,
                       condition:(String, String)=("", "")) extends SearchField {

  /** `RequestVar` containing the text selected */
  protected object cell extends RequestVar("") with Serializable {
    override def __nameSalt = "select_reqvar_cell_" + table +"_" + field
  }

  def getValue = if(cell.get.nonEmpty) Some(cell.get) else None
  def setValue(value:Any){cell.set(value.toString)}
  def resetValue = cell.set("")

  def sqlRequest(attr:String) = {
    val attr1 = if (attr=="") "" else attr+"."
    if (cell.get.nonEmpty) Some(("UPPER("+attr1+field+") LIKE ?", ("%"+cell.get.toUpperCase+"%")::Nil))
    else None
  }

  /** Getter for suggestions from the database
   *
   * @param current The input from user
   * @return List of suggestions
   */
  private def getSugggestions(current: String):List[String] = {
      if (current.isEmpty)
        Nil
      else condition match {
        case (_, "") =>
          DB.runQuery("SELECT DISTINCT "+field+" FROM "+table+" WHERE UPPER("+table+"."+field+") LIKE ? ORDER BY " + table + "." + field + " ASC LIMIT 0 , "+limit, ("%"+current.toUpperCase+"%")::Nil)._2.map(_.head)
        case ("", cond) =>
          DB.runQuery("SELECT DISTINCT "+field+" FROM "+table+" WHERE UPPER("+table+"."+field+") LIKE ? AND "+ cond + " ORDER BY " + table + "." + field + " ASC LIMIT 0 , "+limit, ("%"+current.toUpperCase+"%")::Nil)._2.map(_.head)
        case (tab, cond) =>
          DB.runQuery("SELECT DISTINCT "+field+" FROM "+table+", "+tab+" WHERE UPPER("+table+"."+field+") LIKE ? AND "+cond+" ORDER BY " + table + "." + field + " ASC LIMIT 0 , "+limit, ("%"+current.toUpperCase+"%")::Nil)._2.map(_.head)
      }
  }

  protected def autoCompleteJs = AnonFunc("term, res",JsRaw(
    (S.fmapFunc(S.contextFuncBuilder(SFuncHolder({ terms: String =>
      val _candidates =
        if(terms != null && terms.trim() != "")
          getSugggestions(terms)
        else
          Nil
      JsonResponse(JArray(_candidates map { c => JString(c)/*.toJson*/ }))
    })))
    ({ name =>
      "liftAjax.lift_ajaxHandler('" + name
    })) +
    "=' + encodeURIComponent(term), " +
    "function(data){ res(data); }" +
    ", null, 'json');"))

  /** XML when there is a default value
   *
   * @param default default value
   * @param readOnly readOnly mode
   * @param reset reset the input
   * @return the `NodeSeq` containing the xml
   */
  def xmlWithDefault(default: String, readOnly: Boolean, reset: Boolean) = {
    val id = table+"-"+field+"-autocomplete"
    val defaultVal = if (reset || cell.get.isEmpty) default else cell.get
    //val readonlyAttr = if (readonly) "readonly" -> "readonly" else "mmcwrite" -> ""
    val attrs = if (readOnly) Seq("id" -> id, "size" -> size.toString, "readonly" -> "readonly") else Seq("id" -> id, "size" -> size.toString)
    Script(OnLoad(JsRaw("jQuery('#"+id+"').createAutocompleteField("+autoCompleteJs.toJsCmd+")").cmd)) ++
      SHtml.text(defaultVal, s=>{cell.set(s)}, attrs: _*)
  }

  /** XML when there is a default value and callback
   *
   * @param defaultVal the default value
   * @param f the function to apply to the selected value
   * @param readOnly readOnly mmode
   * @return the `NodeSeq` containing the xml
   */
  def xmlWithDefaultAndCallback(defaultVal: String, f: Function1[String, Unit], readOnly: Boolean = false) = {
    val id = table+"-"+field+"-autocomplete"
    val attrs = if (readOnly) Seq("id" -> id, "size" -> size.toString, "readonly" -> "readonly") else Seq("id" -> id, "size" -> size.toString)
    Script(OnLoad(JsRaw("jQuery('#"+id+"').createAutocompleteField("+autoCompleteJs.toJsCmd+")").cmd)) ++
      SHtml.text(defaultVal, s=>{f(s)}, attrs: _*)
  }

  /** Default xml for the autocomplete */
  def xml = {
    val id = table+"-"+field+"-autocomplete"
    Script(OnLoad(JsRaw("jQuery('#"+id+"').createAutocompleteField("+autoCompleteJs.toJsCmd+")"))) ++
    SHtml.ajaxText(cell.get, s=>{ cell.set(s); Noop}, "id" -> id, "size" ->  size.toString)
  }
}


/**
 * Slider
 */

case class Slider(val name:String, val table:String, val field:String, func:String="") extends SearchField {

  /** Minumum value for the slider */
  private def min =
    if(func.nonEmpty) DB.runQuery("SELECT "+func+"(MIN("+field+")) FROM "+table)._2.head.head.toInt
    else DB.runQuery("SELECT MIN("+field+") FROM "+table)._2.head.head.toInt
  /** Maximum value for the slider */
  private def max =
    if(func.nonEmpty) DB.runQuery("SELECT "+func+"(MAX("+field+")) FROM "+table)._2.head.head.toInt
    else DB.runQuery("SELECT MAX("+field+") FROM "+table)._2.head.head.toInt

  private def init = (min, max)

  /** `RequestVar` containing the range selected, of format (min, max) */
  private object cell extends RequestVar(init) with Serializable {
    override def __nameSalt = "select_reqvar_cell_" + table + field
  }

  def getValue = if(cell.get!= (min, max)) Some(cell.get) else None
  def setValue(value:Any){cell.set(value.asInstanceOf[(Int,Int)])}
  def resetValue = cell.set(init)

  /** The id of the text field */
  private val id = randomString(12)

  def sqlRequest(attr:String) = {
    val attr1 = if (attr=="") "" else attr + "."
    if (min>max) None
    else cell.get match{
      case (x, y) if (x==min & y==max)=> None
      case (x, y) if (x==min)=>
        if(func.nonEmpty) Some((func+"("+attr1+field+")<=?", y.toString::Nil))
        else Some((attr1+field+"<=?", y.toString::Nil))
      case (x,y) if (y==max)=>
        if(func.nonEmpty) Some((func+"("+attr1+field+")>=?", x.toString::Nil))
        else Some((attr1+field+">=?", x.toString::Nil))
      case (x, y) =>
        if(func.nonEmpty) Some((func+"("+attr1+field+")<=? AND " + func+"("+attr1+field+")>=?",y.toString::x.toString::Nil))
        else Some((attr1+field+"<=? AND " + attr1+field+">=?", y.toString::x.toString::Nil))
    }
  }

  // Initial values for the slider
  //def init = cell.get._1 + ";" + cell.get._2

  /*def xml = {
    /** Callback to get the selected value from slider (via Javascript) */
    val cb = SHtml.ajaxCall(JsRaw("value"), computeValues)
    /** Script for ther slider */
    val script = Script(new JsCmd{
      def toJsCmd = "$(function() {"+
        "$(\"#"+id+"\").slider({ "+
        "from: "+ min +", "+
        "to: "+ max +", "+
        "step: 1, "+
        "skin: \"plastic\", " +
        "smooth: false, "    +
        "dimension: \'\', "  +
        "callback: function(value) { " +
          cb._2.toJsCmd + ";"  +
        "}"+
        "})});"
    })

    if (max>min){
      {script} ++ <div style="margin-top: 10px;"><input id={id} type="slider" value= {init}/></div>
    } else {
      <i> {min} </i>
    }
  }*/
  def xml: NodeSeq = {
    val cb = SHtml.ajaxCall(JsRaw("yearValue"), computeValues)
    val script = Script(new JsCmd {
      def toJsCmd = "$(function() {"+
        "$(\"#"+ id +"\").slider({ "+
        "range: true, "+
        "min: "+ min +", "+
        "max: "+ max +", " +
        "values: ["+ min +"," + max + "], " +
        "change: function(event, ui) {" +
        "updateValues(event,ui);" +
        "var yearValue = ui.values[0]+';'+ui.values[1];" +
        cb._2.toJsCmd +
        "}," +
        "slide: function(event, ui) {" +
        "updateValues(event,ui);" +
        "}" +
        "});" +
        "function updateValues(event, ui) { " +
        "var offset1 = $('.ui-slider-handle:first').css('left'); " +
        "var offset2 = $('.ui-slider-handle:last').css('left'); " +
        "var tickerWidth = $('.sliderLabel').css('width');" +
        "var value1 = ui.values[ 0 ]; " +
        "var value2 = ui.values[ 1 ]; " +
        "var yearDiff = value2 - value1; " +
        "var specialOffsetMin = 0, specialOffsetMax = 0;" +
        "if (yearDiff > 0 && yearDiff < 3) {" +
        "if (value1 < 1980) {" +
        "specialOffsetMax = 10;" +
        "} else {" +
        "specialOffsetMin = 10;" +
        "}" +
        "}" +
        "var offset1Int = offset1.substring(0, offset1.length - 2);" +
        "var offset2Int = offset2.substring(0, offset2.length - 2);" +
        "var widthInt = tickerWidth.substring(0, tickerWidth.length - 2);" +
        "var correctedOffset1 = offset1Int - 8 - specialOffsetMin; " +
        "var correctedOffset2 = offset2Int - widthInt - 8 + specialOffsetMax; " +
        "$('#value1').text(value1).css({'left':correctedOffset1 +'px'}); " +
        "$('#value2').text(value2).css({'left':correctedOffset2 +'px'});" +
        "}" +
        "});"
    })
    if (max>min) {
      //{script} ++ <div style="margin-top: 10px;"><input id={inputHtmlId} type="slider" value={init} /></div>
      val labelDiv = {<div style="height: 10px"><span id="value1" class="sliderLabel">{min}</span><span id="value2" class="sliderLabel" style="left: 96%">{max}</span></div>}
      val sliderDiv = {<div style="margin-top: 10px;" id={id}></div>}
      {script} ++ <div class="sliderBarContainer">{labelDiv}{sliderDiv}</div>
    } else {
      <i> {min} </i>
    }
  }

  /** Function to compute the request and recompute the results
   *
   * @param value String returned via the Script
   */
  def computeValues(value:String) = {
    val tmp = value.splitAt(";").head
    cell.set((tmp._1.toInt, tmp._2.toInt))
    Noop
  }
}