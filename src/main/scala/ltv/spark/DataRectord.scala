package ltv.spark

import java.net.URI

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import scala.util.{Failure, Success, Try}


sealed trait DataRecord

final case class HostDataRecord(
                           scheme: String, // http, https or all
                           host: String, // Host without www.
                           rhost: String // Reversed host without www.
                         ) extends DataRecord

object HostDataRecord {

  /**
    * From the string in format <host> <id> {<json string>}, e.g.:
    * 0,0,0,1)/ 20180422003525 {"url": "http://1.000.000.000/", "mime": "text/html", "mime-detected": "text/html", "status": "403", "digest": "77UZB2ZYF6IM5EC3JWTYAE4KC6VML6CH", "length": "1866", "offset": "3213", "filename": "crawl-data/CC-MAIN-2018-17/segments/1524125945484.57/crawldiagnostics/CC-MAIN-20180422002521-20180422022521-00457.warc.gz"}
    *
    * @param s Formatted string
    * @return
    */

  def fromString(s: String): Option[HostDataRecord] = {

    implicit val formats: DefaultFormats = DefaultFormats
    val json = s.split(' ').drop(2).mkString(" ")

    val data = for (
      json <- Try(parse(json).extract[Map[String, String]]).toOption;
      url <- json.get("url");
      uri <- Try(URI.create(url)).toOption;
      h <- Option(uri.getHost);
      s <- Option(uri.getScheme)
    ) yield {
      val host = if (h.startsWith("www.")) h.drop(4) else h
      val rhost = host.split('.').reverse.mkString(".")
      /* Reversed */
      HostDataRecord(scheme = s, host = host, rhost = rhost)
    }
    data
  }

  def fromLGString(s: String): Option[HostDataRecord] = {
    Try {
      if (s.startsWith("#")) None
      else {
        val scheme = "all"
        val rhost = s.split('\t').last
        val host = rhost.split('.').reverse.mkString(".")
        Some(
          /* Already reversed in CC linkgraph */
          HostDataRecord(scheme = scheme, host = host, rhost = rhost)
        )
      }
    } match {
      case Success(res) => res
      case Failure(f) => None
    }
  }

  def fromDomainLGString(s: String): Option[HostDataRecord] = {
    Try {
      if (s.startsWith("#")) None
      else {
        val scheme = "all"
        val rhost = s.split('\t')(1) //TODO: Exception here
        val host = rhost.split('.').reverse.mkString(".")
        Some(
          /* Already reversed in CC linkgraph */
          HostDataRecord(scheme = scheme, host = host, rhost = rhost)
        )
      }
    } match {
      case Success(res) => res
      case Failure(f) => None
    }
  }

  def generatePK(hdr: HostDataRecord): String = {
    hdr.rhost + "#" + hdr.scheme
  }

}


