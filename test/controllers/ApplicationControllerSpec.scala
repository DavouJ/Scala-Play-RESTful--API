package controllers


import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.mvc.ControllerComponents
import play.api.test.Helpers._
import repositories.DataRepository
import uk.gov.hmrc.mongo.MongoComponent

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ApplicationControllerSpec @Inject()(dataRepository: DataRepository)(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(dataRepository)(controllerComponents)

  "ApplicationController.index()" should {

    val result = TestApplicationController.index()(FakeRequest())

    "return yo" in {
      status(result) shouldBe Status.OK
    }
  }

  "AplicationController.create()" should {

  }

  "AplicationController.read()" should {

  }

  "AplicationController.update(id: String)" should {

  }

  "AplicationController.delete(id: String)" should {

  }


}
