package ltv.spark

import com.google.inject.{Guice, Injector}

object Run extends App {

  val injector: Injector = Guice.createInjector(new DependencyModule {})

  val importComponent = injector.getInstance(classOf[CCImport])

  importComponent.run()

}
