// Sbt eclipse plugin

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0-M2")

// Web plugin

libraryDependencies <+= sbtVersion(v => v match {
case "0.11.2" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.2-0.2.11"
case "0.11.3" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.3-0.2.11.1"
})

// Deploy one-jar plugin

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.3")

resolvers += Resolver.url(
  "sbt-plugin-releases",
  url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.untyped" %% "sbt-runmode" % "0.4") 

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.1.0")

