package ltv.spark

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

trait DependencyModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {

    bind[ReaderComponent].to[HdfsReader]
    bind[SaverComponent].to[HdfsSaver]

  }
}
