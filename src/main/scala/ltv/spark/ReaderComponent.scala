package ltv.spark

import org.apache.hadoop.fs.Path
import org.apache.spark.rdd.RDD

trait ReaderComponent {

  /**
    * Imports data
    * @param index Index
    * @return
    */
  def read(index: String): RDD[HostDataRecord]

}

/**
  * Reads index data from hdfs
  */
class HdfsReader extends ReaderComponent {

  val uri: String = config.getString("hdfs.uri")
  val inputPattern: String = config.getString("hdfs.input_pattern")

  override def read(index: String): RDD[HostDataRecord] = {
    val pattern: String = inputPattern
    val path: Path = new Path(uri, pattern)
    val rdd = sc.textFile(pattern)
      .flatMap {
        s: String => HostDataRecord.fromString(s)
      }
    rdd
  }

}

/**
  * Reads link graph data from hdfs
  */
trait HdfsDomainGraphReader extends ReaderComponent {

  val uri: String = config.getString("hdfs.uri")
  val inputPattern: String = config.getString("hdfs.input_pattern")

  override def read(index: String): RDD[HostDataRecord] = {
    val pattern = inputPattern.format(index)
    val path: Path = new Path(uri, pattern)
    val rdd = sc.textFile(pattern)
      .flatMap {
        s: String => HostDataRecord.fromLGString(s)
      }
    rdd
  }

}

/**
  * Reads index data from S3
  */
class S3Reader extends ReaderComponent {

  val inputPattern: String = config.getString("s3.input_pattern")

  override def read(index: String): RDD[HostDataRecord] = {
    val rdd = sc.textFile(inputPattern)
      .flatMap {
        s: String => HostDataRecord.fromString(s)
      }
    rdd
  }

}
