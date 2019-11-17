package ltv.spark

import com.google.inject.Inject
import javax.inject.Singleton
import org.apache.spark.rdd.RDD

import scala.collection.JavaConverters._

trait CCImportComponent {
  def run(): Unit
}

@Singleton
class CCImport @Inject() (reader: ReaderComponent, saver: SaverComponent) extends CCImportComponent {

  val indexes: List[String] = config.getStringList("indexes").asScala.toList

  /**
    * Index name in format "CC-MAIN-2017-17"
    *
    * @param index Index name
    * @return "1717"
    */
  private def getColumnName(index: String): String = {
    val splitted = index.split('-')
    splitted(2).takeRight(2) + splitted(3)
  }

  def run(): Unit = {
    /* Indexes to import */
    for (index <- indexes) {
      val rdd: RDD[HostDataRecord] = reader.read(index)
      saver.save(getColumnName(index), rdd)
    }
  }

}