//package controllers
//
//import akka.util.ByteString
//import baseSpec.BaseSpecWithApplication
//import models.DataModel
//import org.scalamock.scalatest.MockFactory
//import play.api.http.Status
//import play.api.libs.json.{JsSuccess, JsValue, Json}
//import play.api.libs.streams.Accumulator
//import play.api.mvc.Results._
//import play.api.mvc.{ControllerComponents, Result}
//import play.api.test.FakeRequest
//import play.api.test.Helpers.{contentAsJson, _}
//import services.{LibraryService, RepositoryService}
//
//import javax.inject.Inject
//import scala.concurrent.{ExecutionContext, Future}
//
//class ApplicationControllerSpecV2 extends BaseSpecWithApplication with MockFactory {
//
//
//  val TestApplicationController = new ApplicationController(
//    component,
//    app.injector.instanceOf[LibraryService],
//    app.injector.instanceOf[RepositoryService]
//  )
//
//
//  private val dataModel: DataModel = DataModel(
//    "abcd",
//    "test name",
//    "test description",
//    100,
//    "testurl"
//  )
//
//
//  "ApplicationController.index()" should {
//    "repository is successful" when {
//      "return OK status with items as Json" in {
//        val result = TestApplicationController.index()(FakeRequest())
//
//        status(result) shouldBe Status.OK
//      }
//    }
//  }
//
//  "ApplicationController.index()" should {
//    "repository fails" when {
//      "return INTERNAL SERVER ERROR status" in {
//        val result = TestApplicationController.index()(FakeRequest())
//
//        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
//      }
//    }
//  }
//
////  "HomeController GET" should {
////    "udfhuksdf" in {
////      true
////    }
////  }
//
////  "AplicationController.create()" should {
////
////    "Create a book in the database" in {
////      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
////      val createdResult: Future[Result] = TestApplicationController.create()(request)
////
////      status(createdResult) shouldBe Status.CREATED
////    }
////  }
////
////  "AplicationController.read()" should {
////
////    "Find a book in the database by id" in {
////      beforeEach()
////      val request: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
////      val createdResult: Future[Result] = TestApplicationController.create()(request)
////
////      status(createdResult) shouldBe Status.CREATED
////
////      val readResult: Future[Result]  = TestApplicationController.readById("abcd")(FakeRequest())
////
////      status(readResult) shouldBe Status.OK
////
////      val actualJson = contentAsJson(readResult)
////      actualJson shouldBe Json.toJson(dataModel)
////
////      afterEach()
////    }
////
////  }
////
////  "AplicationController.update(id: String)" should {
////    "Update a book's details by id" in {
////      beforeEach()
////      val request: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
////      val createdResult: Future[Result] = TestApplicationController.create()(request)
////
////      status(createdResult) shouldBe Status.CREATED
////
////      val updateResult: Accumulator[ByteString, Result] = TestApplicationController.update("abcd")(FakeRequest())
////
////      status(updateResult) shouldBe Accepted {Json.toJson(dataModel)}
////      //contentAsJson(updateResult).as[DataModel] shouldBe dataModel
////      val actualJson = contentAsJson(updateResult)
////      actualJson shouldBe Json.toJson(dataModel)
////
////      afterEach()
////    }
////  }
////
////  "AplicationController.delete(id: String)" should {
////
////    "remove a book from the collection by id" in{
////      beforeEach()
////      val request: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
////      val createdResult: Future[Result] = TestApplicationController.create()(request)
////
////      status(createdResult) shouldBe Status.CREATED
////
////      val deleteResult: Future[Result]  = TestApplicationController.delete("abcd")(FakeRequest())
////
////      val actualJson = contentAsJson(deleteResult)
////      actualJson shouldBe JsSuccess(Accepted)
////
////      afterEach()
////    }
////
////  }
//
//  override def beforeEach(): Unit = await(repository.deleteAll())
//
//  override def afterEach(): Unit = await(repository.deleteAll())
//}