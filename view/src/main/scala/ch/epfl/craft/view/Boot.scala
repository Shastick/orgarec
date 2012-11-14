package ch.epfl.craft.view

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.storage.db.PGDBFactory
import ch.epfl.craft.view.snippet.GraphApi
import ch.epfl.craft.view.view.Site


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot extends Bootable {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("ch.epfl.craft.view")

    // Build SiteMap
    def sitemap = SiteMap(
      Menu.i("Home") / "index",
      Site.topicDetail
    )

    LiftRules.setSiteMap(sitemap)
    
    // Set default DB:
    
    DBFactory.setDefault(new PGDBFactory(
        Props.get("pg-host").get,
        Props.get("pg-dbname").get,
        Props.get("pg-uname").get,
        Props.get("pg-pwd").get
    ))
    // Use jQuery 1.4
    //LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts

    //Show the spinny image when an Ajax call starts
    //LiftRules.ajaxStart =
    //  Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    //LiftRules.ajaxEnd =
    //  Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.statelessDispatchTable.append(GraphApi)

  }
}
