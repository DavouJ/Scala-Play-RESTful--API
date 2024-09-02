package service

import baseSpec.BaseSpec
import cats.data.EitherT
import connectors.LibraryConnector
import models.{APIError, ApiDataModel, DataModel, VolumeInfo}
import play.api.http.Status
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json, OFormat}
import services.LibraryService
import org.scalatest._

import scala.concurrent.{ExecutionContext, Future}
import scala.tools.nsc.interactive.Response


class LibraryServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite {

  val mockConnector = mock[LibraryConnector]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testService = new LibraryService(mockConnector)

  val gameOfThrones: JsValue = Json.obj(
    "id" -> "someId",
    "volumeInfo" -> Json.obj(
      "title" -> "A Game of Thrones",
      "description" -> "The best book!!!",
      "pageCount" -> 100,
      "imageLinks" -> Json.obj(
        "thumbnail" -> "thumbnail URL"
      )
    )
  )

  "getGoogleBook" should {
    val url: String = "testUrl"

    "return a book" in {

      (mockConnector.get[ApiDataModel](_: String)(_: OFormat[ApiDataModel], _: ExecutionContext))
        .expects(url, *, *)
        .returning(EitherT.rightT(gameOfThrones.as[ApiDataModel]))
        .once()

      whenReady(testService.getGoogleBook(urlOverride = Some(url), term = "someId").value) { result =>
        result shouldBe Right(gameOfThrones.as[ApiDataModel])
      }
    }

    "return error" in{
      val url: String = "testUrl"

      (mockConnector.get[ApiDataModel](_: String)(_: OFormat[ApiDataModel], _: ExecutionContext))
        .expects(url, *, *)
        .returning(EitherT.leftT(APIError.BadAPIResponse(500, "Could not connect")))// How do we return an error?
        .once()

      whenReady(testService.getGoogleBook(urlOverride = Some(url), term = "someId").value) { result =>
        result shouldBe Left(APIError.BadAPIResponse(500, "Could not connect"))
      }
    }
  }
}
