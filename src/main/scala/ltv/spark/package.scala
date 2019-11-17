package ltv

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SparkSession}

package object spark {

  val config: Config = ConfigFactory.load()

  val spark: SparkSession = SparkSession.builder().config(new SparkConf()
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .set("fs.s3a.access.key", config.getString("s3.access_key"))
    .set("fs.s3a.secret.key", config.getString("s3.secret_key"))
    .set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .set("spark.hadoop.validateOutputSpecs", "false")
    .set("spark.driver.maxResultSize", "5g")
  )
    .getOrCreate()

  implicit val sc: SparkContext = spark.sparkContext
  implicit val sqlContext: SQLContext = spark.sqlContext

}
