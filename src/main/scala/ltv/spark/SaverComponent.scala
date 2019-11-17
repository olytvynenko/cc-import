package ltv.spark

import org.apache.hadoop.conf.Configuration
import org.apache.spark.rdd.RDD
import it.nerdammer.spark.hbase._

trait SaverComponent {

  /**
    * Saves imported data
    * @param index Index
    * @param rdd Spark RDD
    */
  def save(index: String, rdd: RDD[HostDataRecord]): Unit

}

/**
  * Saves data to HBase
  */
class HBaseSaverComponent extends SaverComponent {

  val hbaseConf: Configuration = new Configuration()
  hbaseConf.setInt("timeout", 120000)
  System.setProperty("user.name", "hdfs")
  System.setProperty("HADOOP_USER_NAME", "hdfs")
  val table: String = config.getString("hbase_table")
  val family: String = config.getString("hbase_family")

  override def save(column: String, rdd: RDD[HostDataRecord]): Unit = {

    val outRdd: RDD[(String, Boolean)] =
      rdd
        .mapPartitions { iter: Iterator[HostDataRecord] => {
          iter.map { h => (HostDataRecord.generatePK(h), true) }
        }
        }

    outRdd
      .toHBaseTable(table)
      .toColumns(column)
      .inColumnFamily(family)
      .save
  }

}

/**
  * Saves data to hdfs
  */
class HdfsSaver extends SaverComponent {

  val outPath: String = config.getString("out_path")

  override def save(index: String, rdd: RDD[HostDataRecord]): Unit = {

    rdd
      .map(hr => hr.host)
      .saveAsTextFile(outPath.replaceAll("/$", "") + s"/$index")
  }

}
