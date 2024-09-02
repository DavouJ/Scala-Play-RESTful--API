package controllers

import baseSpec.BaseSpecWithApplication
import com.mongodb.client.result.DeleteResult
import models.{APIError, DataModel, DatabaseError}
import org.mockito.Mockito.{mock, when}
import org.mongodb.scala.internal.Done
import play.api.http.Status
import play.api.http.Status.NOT_FOUND
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import repositories.DataRepository
import services.{LibraryService, RepositoryService}

import scala.concurrent.Future

class ApplicationControllerSpecV2 extends BaseSpecWithApplication {

  val mockRepositoryService: RepositoryService = mock(classOf[RepositoryService])
  val mockLibraryService: LibraryService = mock(classOf[LibraryService])
  val mcc: ControllerComponents = Helpers.stubControllerComponents()

  override def beforeEach(): Unit = {
    super.beforeEach()
//    when(mockRepositoryService.deleteAll).thenReturn(Future.successful(Right(DeleteResult)))
//    await(mockRepositoryService.deleteAll)
  }

  override def afterEach(): Unit = {
    //await(mockRepositoryService.deleteAll)
    super.afterEach()
  }

  val TestApplicationController = new ApplicationController(mockRepositoryService, mockLibraryService)(executionContext, mcc)

  "ApplicationController.index()" should {
    "the DataRepository successfully retrieves the books" when {
      "return the full list of books" in {

        val testBookList: Seq[DataModel] =
          Seq(
            DataModel("AX88Y8A90AS", "Harry Potter", "A book about magic", 560),
            DataModel("AX884JD0AS", "Biff and Chip", "A beginners book for learning to read", 12)
          )

        when(mockRepositoryService.index())
          .thenReturn(Future.successful(Right(testBookList)))

        val result = TestApplicationController.index()(FakeRequest())
        status(result) shouldBe Status.OK
        contentAsJson(result) shouldBe Json.toJson(testBookList)
      }

      "the DataRepository is unsuccessful in retrieving the book list" when {
        "return an API error" in {

          when(mockRepositoryService.index())
            .thenReturn(Future.successful(Left(DatabaseError.BadAPIResponse(NOT_FOUND, "Books can't be found"))))

          val result = TestApplicationController.index()(FakeRequest())
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
          //contentAsJson(result) shouldBe Json.toJson(DatabaseError.BadAPIResponse(NOT_FOUND, "Books can't be found"))
        }
      }
    }
  }
}