package ch.epfl.craft.view

import net.liftweb.sitemap._
import net.liftweb.http._
import ch.epfl.craft.recom.storage.db.DBFactory
import ch.epfl.craft.recom.storage.db.PGDBFactory
import ch.epfl.craft.view.view.Site
import net.liftweb.util.Props


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
      Menu.i("About") / "about",
      Menu.i("Graph Explorer") / "graph",
      Menu.i("SHS Explorer") / "shs",
      Menu.i("Section Explorer") / "section", 
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

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

  }
}
