package controllers


import akka.util.ByteString
import baseSpec.BaseSpecWithApplication
import cats.data.EitherT
import models.{APIError, ApiDataModel, DataModel, DatabaseError, UpdateModel}
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.libs.json.{JsSuccess, JsValue, Json, OFormat}
import play.api.libs.streams.Accumulator
import play.api.libs.ws.body
import play.api.mvc.Results._
import play.api.mvc.{AnyContentAsEmpty, ControllerComponents, Result}
import play.api.test.Helpers.{contentAsJson, _}
import services.{LibraryService, RepositoryService}
import uk.gov.hmrc.mongo.MongoComponent

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ApplicationControllerSpec  extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(component, service, repository)



  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test description",
    100,
    "testurl"
  )

   private val update: UpdateModel = UpdateModel(
     "name",
     "name test"
   )




  "ApplicationController.index()" should {
    beforeEach()

    val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
    val createdResult: Future[Result] = TestApplicationController.create()(request)

    status(createdResult) shouldBe Status.CREATED

    "return a Json with all the stored books and ok status" in {


      val request2 = buildPost("/api")
      val indexResult  = TestApplicationController.index()(request2)

      status(indexResult) shouldBe Status.OK
      //should put a test to asset expected json body returned
      afterEach()
    }



  }


  "AplicationController.create()" should {

    "Create a book in the database and created status" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED
      afterEach()
    }
  }

  "AplicationController.readById()" should {
    beforeEach()
    val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
    val createdResult: Future[Result] = TestApplicationController.create()(request)

    status(createdResult) shouldBe Status.CREATED

    val request2 = buildPost(s"/api/${dataModel._id}")
    val readResult: Future[Result]  = TestApplicationController.readById("abcd")(request2)

    "Find a book in the database by id and return ok status" in {

      status(readResult) shouldBe Status.OK

      val actualJson = contentAsJson(readResult)
      actualJson shouldBe Json.toJson(dataModel)
      afterEach()

    }

  }

  "AplicationController.update(id: String)" should {

    val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
    val createdResult: Future[Result] = TestApplicationController.create()(request)

    status(createdResult) shouldBe Status.CREATED

    "Update a book's details by id and return accepted status" in {
      beforeEach()


      val request2 = buildPost(s"/api/update/abcd").withBody[JsValue](Json.toJson(update))
      val updateResult = TestApplicationController.update("abcd")(request2)

      status(updateResult) shouldBe Status.ACCEPTED

      afterEach()
    }
  }

  "AplicationController.delete(id: String)" should {
    beforeEach()
    val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
    val createdResult: Future[Result] = TestApplicationController.create()(request)

    status(createdResult) shouldBe Status.CREATED

    "remove a book from the collection by id and return accepted status" in{

      val request2 = buildPost(s"/api/delete/abcd")
      val deleteResult  = TestApplicationController.delete("abcd")(request2)

      status(deleteResult) shouldBe Status.ACCEPTED

      afterEach()
    }

    "throw a 505 error" in{

      val request2 = buildPost(s"/api/delete/abcdef")
      val deleteResult  = TestApplicationController.delete("abcdef")(request2)

      status(deleteResult) shouldBe DatabaseError.BadAPIResponse

      afterEach()
    }

  }

  override def beforeEach(): Unit = await(repository.deleteAll)

  override def afterEach(): Unit = await(repository.deleteAll)
}