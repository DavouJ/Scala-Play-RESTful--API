//package controllers
//
//
//import akka.util.ByteString
//import baseSpec.BaseSpecWithApplication
//import models.{DataModel, VolumeInfo}
//import play.api.test.FakeRequest
//import play.api.http.Status
//import play.api.libs.json.{JsSuccess, JsValue, Json}
//import play.api.libs.streams.Accumulator
//import play.api.mvc.Results._
//import play.api.mvc.{AnyContentAsEmpty, ControllerComponents, Result}
//import play.api.test.Helpers.{contentAsJson, _}
//import repositories.DataRepository
//import services.LibraryService
//import uk.gov.hmrc.mongo.MongoComponent
//
//import javax.inject.Inject
//import scala.concurrent.{ExecutionContext, Future}
//
//class ApplicationControllerSpec @Inject()(dataRepository: DataRepository)(val controllerComponents: ControllerComponents)(libraryService: LibraryService)(implicit ec: ExecutionContext) extends BaseSpecWithApplication {
//
//  val TestApplicationController = new ApplicationController(dataRepository)(controllerComponents)(libraryService)
//
//  private val volumeInfo: VolumeInfo = VolumeInfo(
//    "test name",
//    Some("test description"),
//    100
//  )
//  private val dataModel: DataModel = DataModel(
//    "abcd",
//    volumeInfo
//  )
//
//
//  "ApplicationController.index()" should {
//    beforeEach()
//    val result = TestApplicationController.index()(FakeRequest())
//
//    "return yo" in {
//      beforeEach()
//      status(result) shouldBe Status.OK
//      afterEach()
//    }
//  }
//
//  "AplicationController.create()" should {
//
//    "Create a book in the database" in {
//      beforeEach()
//      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Future[Result] = TestApplicationController.create()(request)
//
//      status(createdResult) shouldBe Status.CREATED
//      afterEach()
//    }
//  }
//
//  "AplicationController.read()" should {
//
//    "Find a book in the database by id" in {
//      beforeEach()
//      val request: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Future[Result] = TestApplicationController.create()(request)
//
//      status(createdResult) shouldBe Status.CREATED
//
//      val readResult: Accumulator[ByteString, Result]  = TestApplicationController.read("abcd")(FakeRequest())
//
//      status(readResult) shouldBe Status.OK
//
//      val actualJson = contentAsJson(readResult)
//      actualJson shouldBe Json.toJson(dataModel)
//
//      afterEach()
//    }
//
//  }
//
//  "AplicationController.update(id: String)" should {
//    "Update a book's details by id" in {
//      beforeEach()
//      val request: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Future[Result] = TestApplicationController.create()(request)
//
//      status(createdResult) shouldBe Status.CREATED
//
//      val updateResult: Accumulator[ByteString, Result] = TestApplicationController.update("abcd")(FakeRequest())
//
//      status(updateResult) shouldBe Accepted {Json.toJson(dataModel)}
//      //contentAsJson(updateResult).as[DataModel] shouldBe dataModel
//      val actualJson = contentAsJson(updateResult)
//      actualJson shouldBe Json.toJson(dataModel)
//
//      afterEach()
//    }
//  }
//
//  "AplicationController.delete(id: String)" should {
//
//    "remove a book from the collection by id" in{
//      beforeEach()
//      val request: FakeRequest[JsValue] = buildPost(s"/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Future[Result] = TestApplicationController.create()(request)
//
//      status(createdResult) shouldBe Status.CREATED
//
//      val deleteResult: Accumulator[ByteString, Result]  = TestApplicationController.delete("abcd")(FakeRequest())
//
//      val actualJson = contentAsJson(deleteResult)
//      actualJson shouldBe JsSuccess(Accepted)
//
//      afterEach()
//    }
//
//  }
//
//  override def beforeEach(): Unit = await(repository.deleteAll())
//
//  override def afterEach(): Unit = await(repository.deleteAll())
//}
