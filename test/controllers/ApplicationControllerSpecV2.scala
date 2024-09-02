package controllers

import baseSpec.BaseSpecWithApplication
import models.{APIError, DataModel}
import org.mockito.Mockito.{mock, when}
import org.mongodb.scala.internal.Done
import play.api.http.Status
import play.api.http.Status.NOT_FOUND
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import repositories.DataRepository
import services.LibraryService

import scala.concurrent.Future

class ApplicationControllerSpecV2 extends BaseSpecWithApplication {

  val mockDataRepository: DataRepository = mock(classOf[DataRepository])
  val mockLibraryService: LibraryService = mock(classOf[LibraryService])
  val mcc: ControllerComponents = Helpers.stubControllerComponents()

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockDataRepository.deleteAll()).thenReturn(Future.successful(Done))
    await(mockDataRepository.deleteAll())
  }

  override def afterEach(): Unit = {
    await(mockDataRepository.deleteAll())
    super.afterEach()
  }

  val TestApplicationController = new ApplicationController(mockDataRepository, mockLibraryService)(executionContext, mcc)

  "ApplicationController.index()" should {
    "the DataRepository successfully retrieves the books" when {
      "return the full list of books" in {

        val testBookList: Seq[DataModel] =
          Seq(
            DataModel("AX88Y8A90AS", "Harry Potter", Some("A book about magic"), 560),
            DataModel("AX884JD0AS", "Biff and Chip", Some("A beginners book for learning to read"), 12)
          )

        when(mockDataRepository.index())
          .thenReturn(Future.successful(Right(testBookList)))

        val result = TestApplicationController.index()(FakeRequest())
        status(result) shouldBe Status.OK
        contentAsJson(result) shouldBe Json.toJson(testBookList)
      }

      "the DataRepository is unsuccessful in retrieving the book list" when {
        "return an API error" in {

          when(mockDataRepository.index())
            .thenReturn(Future.successful(Left(APIError.BadAPIResponse(NOT_FOUND, "Books can't be found"))))

          val result = TestApplicationController.index()(FakeRequest())
          status(result) shouldBe Status.NOT_FOUND
        }
      }
    }
  }

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
  //  }e
}
