import sbt.ExclusionRule

/**
  * Check whether OS is windows, if yes set testMode to true
  */

val mc: String = Option(System.getProperty("mainClass")).getOrElse("ltv.spark.Run")

val jar: String = "cc-import.jar"


lazy val root = (project in file("."))
  .settings(
    name := "cc-import",
    version := "1.0",
    scalaVersion := "2.11.8",
    mainClass in assembly := Some(mc),
    assemblyJarName in assembly := jar,
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",
      "org.json4s" %% "json4s-jackson" % "3.2.11",
      "org.apache.hadoop" % "hadoop-aws" % "3.0.0",
      "it.nerdammer.bigdata" % "spark-hbase-connector_2.10" % "1.0.3",
      "org.apache.hbase" % "hbase-common" % "1.0.3" excludeAll(ExclusionRule(organization = "javax.servlet", name = "javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name = "jetty"), ExclusionRule(organization = "org.mortbay.jetty", name = "servlet-api-2.5")),
      "org.apache.hbase" % "hbase-client" % "1.0.3" excludeAll(ExclusionRule(organization = "javax.servlet", name = "javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name = "jetty"), ExclusionRule(organization = "org.mortbay.jetty", name = "servlet-api-2.5")),
      "org.apache.hbase" % "hbase-server" % "1.0.3" excludeAll(ExclusionRule(organization = "javax.servlet", name = "javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name = "jetty"), ExclusionRule(organization = "org.mortbay.jetty", name = "servlet-api-2.5")),
      "junit" % "junit" % "3.8.1",
      "org.scalatest" %% "scalatest" % "3.0.3",
      "org.apache.spark" %% "spark-core" % "2.1.0" % "provided",
      "org.apache.spark" %% "spark-sql" % "2.1.0" % "provided",
      "com.twitter" %% "util-hashing" % "7.1.0",
      "redis.clients" % "jedis" % "2.9.0",
      "com.google.inject" % "guice" % "4.2.2",
      "net.codingwell" %% "scala-guice" % "4.2.6"
    ),
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs@_*) => MergeStrategy.last
      case PathList("javax", "xml", xs@_*) => MergeStrategy.last
      case PathList("javax", "activation", xs@_*) => MergeStrategy.last
      case PathList("javax", "el", xs@_*) => MergeStrategy.last
      case PathList("javax", "inject", xs@_*) => MergeStrategy.last
      case PathList("javax", "ws", xs@_*) => MergeStrategy.last
      case PathList("org", "apache", xs@_*) => MergeStrategy.last
      case PathList("org", "aopalliance", xs@_*) => MergeStrategy.last
      case PathList("org", "objectweb", xs@_*) => MergeStrategy.last
      case PathList("org", "objenesis", xs@_*) => MergeStrategy.last
      case PathList("org", "xml", xs@_*) => MergeStrategy.last
      case PathList("org", "w3c", xs@_*) => MergeStrategy.last
      case PathList("org", "datanucleus", xs@_*) => MergeStrategy.last
      case PathList("com", "google", xs@_*) => MergeStrategy.last
      case PathList("com", "esotericsoftware", xs@_*) => MergeStrategy.last
      case PathList("com", "codahale", xs@_*) => MergeStrategy.last
      case PathList("com", "yammer", xs@_*) => MergeStrategy.last
      case PathList("com", "sun", xs@_*) => MergeStrategy.last
      case PathList("com", "twitter", xs@_*) => MergeStrategy.last
      case PathList("junit", "junit", xs@_*) => MergeStrategy.last
      case "about.html" => MergeStrategy.rename
      case "overview.html" => MergeStrategy.rename
      case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
      case "META-INF/mailcap" => MergeStrategy.last
      case "META-INF/mimetypes.default" => MergeStrategy.last
      case "META-INF/git.properties" => MergeStrategy.last
      case "plugin.properties" => MergeStrategy.last
      case "plugin.xml" => MergeStrategy.last
      case "parquet.thrift" => MergeStrategy.last
      case "log4j.properties" => MergeStrategy.last
      case "mozilla/public-suffix-list.txt" => MergeStrategy.last
      case "mime.types" => MergeStrategy.last
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )