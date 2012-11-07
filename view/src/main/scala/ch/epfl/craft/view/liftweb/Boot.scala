package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import code.snippet.GraphApi
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.storage.db.PGDBFactory


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index",// the simple way to declare a menu
      Menu.i("Monk") / "index-monk"
    )

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    
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
