package controllers

import baseSpec.BaseSpecWithApplication
import com.mongodb.client.result.{DeleteResult, UpdateResult}
import models.{APIError, DataModel, DatabaseError, UpdateModel}
import org.mockito.Mockito.{mock, when}

import play.api.http.Status
import play.api.http.Status.NOT_FOUND
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.ControllerComponents
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.{LibraryService, RepositoryService}

import scala.concurrent.Future

class ApplicationControllerSpecV2 extends BaseSpecWithApplication {

  val mockRepositoryService: RepositoryService = mock(classOf[RepositoryService])
  val mockLibraryService: LibraryService = mock(classOf[LibraryService])
  val mcc: ControllerComponents = Helpers.stubControllerComponents()

//  override def beforeEach(): Unit = {
//    super.beforeEach()
//    when(mockRepositoryService.deleteAll).thenReturn(Future.successful(Right(DeleteResult.acknowledged(1))))
//    await(mockRepositoryService.deleteAll)
//  }
//
//  override def afterEach(): Unit = {
//    await(mockRepositoryService.deleteAll)
//    super.afterEach()
//  }

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
          contentAsJson(result) shouldBe Json.toJson("Bad response from upstream. Got status: 404, and got reason: Books can't be found.")
        }
      }
    }
  }

  "ApplicationController.readById(id: String)" should {
    "the DataRepository successfully retrieves the book by Id" when {
      "return the book" in {

        val testBook: DataModel = DataModel("AX88Y8A90AS", "Harry Potter", "A book about magic", 560)

        when(mockRepositoryService.readById("AX88Y8A90AS")).thenReturn(Future.successful(Right(Some(testBook))))

        val result = TestApplicationController.readById("AX88Y8A90AS")(FakeRequest())
        status(result) shouldBe Status.OK
        contentAsJson(result) shouldBe Json.toJson(testBook)
      }

      "the DataRepository is unsuccessful in retrieving the book by ID" when {
        "return an Database error" in {

          when(mockRepositoryService.readById("badID"))
            .thenReturn(Future.successful(Left(DatabaseError.BadAPIResponse(500, "Book can't be found"))))

          val result = TestApplicationController.readById("badID")(FakeRequest())
          status(result) shouldBe 500
          //contentAsJson(result) shouldBe Json.toJson("Bad response from upstream. Got status: 500, and got reason: Could not find Book")
        }
      }
    }
  }

  "ApplicationController.readByName(name: String)" should {
    "the DataRepository successfully retrieves the book by name" when {
      "return the book" in {

        val testBook: DataModel = DataModel("AX88Y8A90AS", "Harry Potter", "A book about magic", 560)

        when(mockRepositoryService.readByName("Harry Potter")).thenReturn(Future.successful(Right(Some(testBook))))

        val result = TestApplicationController.readByName("Harry Potter")(FakeRequest())
        status(result) shouldBe Status.OK
        contentAsJson(result) shouldBe Json.toJson(testBook)
      }

      "the DataRepository is unsuccessful in retrieving the book by name" when {
        "return an Database error" in {

          when(mockRepositoryService.readByName("badName"))
            .thenReturn(Future.successful(Left(DatabaseError.BadAPIResponse(INTERNAL_SERVER_ERROR, "Book can't be found"))))

          val result = TestApplicationController.readByName("badName")(FakeRequest())
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
          //contentAsJson(result) shouldBe Json.toJson("Bad response from upstream. Got status: 500, and got reason: Could not find Book")
        }
      }
    }
  }


  "ApplicationController.create" should {
    "the DataRepository successfully creates a book" when {
      val testBook: DataModel = DataModel("AX88Y8A90AS", "Harry Potter", "A book about magic", 560)
      val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(testBook: DataModel))
      "create the book" in {

        when(mockRepositoryService.create(testBook)).thenReturn(Future.successful(Right(testBook)))


        val result = TestApplicationController.create()(request)
        status(result) shouldBe Status.CREATED
        contentAsJson(result) shouldBe Json.toJson("Added Harry Potter to database")
      }

      "the DataRepository is unsuccessful in creating" when {
        "return an request error" in {

          val badTestBook: JsValue = Json.obj(
            "nothing" -> 800,
          )


          val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(badTestBook))
          val result = TestApplicationController.create()(request)
          status(result) shouldBe 400
        }
        "return a database error" in {


          when(mockRepositoryService.create(testBook))
            .thenReturn(Future.successful(Left(DatabaseError.BadAPIResponse(500, "Could not create"))))

          val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(testBook))
          val result = TestApplicationController.create()(request)
          status(result) shouldBe 500
          contentAsJson(result) shouldBe Json.toJson("Bad response from upstream. Got status: 500, and got reason: Could not create.")
        }
      }
    }
  }

  //  "ApplicationController.create" should {
  //    "the DataRepository successfully creates a book" when {
  //      "create the book" in {
  //
  //        val testBook: DataModel = DataModel("AX88Y8A90AS", "Harry Potter", "A book about magic", 560)
  //
  //        when(mockRepositoryService.create(testBook)).thenReturn(Future.successful(Right(testBook)))
  //
  //        val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(testBook: DataModel))
  //        val result = TestApplicationController.create()(request)
  //        status(result) shouldBe Status.CREATED
  //        contentAsJson(result) shouldBe Json.toJson("Added Harry Potter to database")
  //      }
  //
  //      "the DataRepository is unsuccessful creating a book" when {
  //        "return an Database error" in {
  //
  //          when(mockRepositoryService.index())
  //            .thenReturn(Future.successful(Left(DatabaseError.BadAPIResponse(500, "Could not create"))))
  //
  //          val result = TestApplicationController.create()(FakeRequest())
  //          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
  //          contentAsJson(result) shouldBe Json.toJson(Status.INTERNAL_SERVER_ERROR, "Bad response from upstream. Got status: 500, and got reason: Could not create")
  //        }
  //      }
  //    }
  //  }

  "ApplicationController.update(id: String)" should {
    "the DataRepository successfully updates" when {
      val updateVals: UpdateModel = UpdateModel("name", "Harry Potter and the Deathly Hallows")
      "update the book" in {
        when(mockRepositoryService.update("AX88Y8A90AS", updateVals)).thenReturn(Future.successful(Right(true)))

        val request: FakeRequest[JsValue] = buildPost("/api/update/:id").withBody[JsValue](Json.toJson(updateVals))
        val result = TestApplicationController.update("AX88Y8A90AS")(request)
        status(result) shouldBe Status.ACCEPTED
        contentAsJson(result) shouldBe Json.toJson("updated")
      }

      "the DataRepository is unsuccessful updating a book" when {
        "return an BadRequest error" in {

          val badUpdate: JsValue = Json.obj(
            "nothing" -> 800,
          )
          val request: FakeRequest[JsValue] = buildPost("/api/update/:id").withBody[JsValue](Json.toJson(badUpdate))

          val result = TestApplicationController.update("AX88Y8A90AS")(request)
          status(result) shouldBe Status.BAD_REQUEST
        }
        "return an Database error" in {

          val request: FakeRequest[JsValue] = buildPost("/api/update/:id").withBody[JsValue](Json.toJson(updateVals))

          when(mockRepositoryService.update("AX88Y8A90AS", updateVals)).thenReturn(Future.successful(Left(DatabaseError.BadAPIResponse(500, "Could not update"))))

          val result = TestApplicationController.update("AX88Y8A90AS")(request)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
          contentAsJson(result) shouldBe Json.toJson("Bad response from upstream. Got status: 500, and got reason: Could not update.")
        }

      }
    }
  }

  "ApplicationController.delete(id: String)" should {
    "the DataRepository successfully deletes" when {
      "delete the book" in {

        when(mockRepositoryService.delete("AX88Y8A90AS")).thenReturn(Future.successful(Right(true)))

        val result = TestApplicationController.delete("AX88Y8A90AS")(FakeRequest())
        status(result) shouldBe Status.ACCEPTED
        contentAsJson(result) shouldBe Json.toJson("Deleted")
      }

      "the DataRepository is unsuccessful deleting" when {

//        when(mockRepositoryService.delete("AX88Y8A90AS"))
//          .thenReturn(Future.successful(Left(DatabaseError.BadAPIResponse(500, "Could not delete"))))
//
//        val result = TestApplicationController.delete("AX88Y8A90AS")(FakeRequest())
//        status(result) shouldBe 500
//        contentAsJson(result) shouldBe Json.toJson("Bad response from upstream. Got status: 500, and got reason: Could not delete.")

      }
    }
  }

}