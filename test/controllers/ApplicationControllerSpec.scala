package controllers


import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.test.Helpers._

class ApplicationControllerSpec extends BaseSpecWithApplication {
  val TestApplicationController = new ApplicationController(
    component
  )

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
