package app

import baseSpec.BaseSpec
import cats.data.EitherT
import connectors.LibraryConnector
import models.{APIError, DataModel, VolumeInfo}
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
      "pageCount" -> 100)
  )

//  "getGoogleBook" should {
//    val url: String = "testUrl"
//
//    //def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): Future[Response] = {
//
//    println(Json.toJson(gameOfThrones))
//
//    "return a book" in {
//      (mockConnector.get[DataModel](_: String)(_:  OFormat[DataModel], _: ExecutionContext))
//        .expects(url, *, *)
//        .returning(EitherT.rightT(gameOfThrones.as[DataModel]))
//        .once()
//
//      whenReady(testService.getGoogleBook(urlOverride = Some(url), term = "someId").value){ result =>
//        result shouldBe Right(gameOfThrones.as[DataModel])
//      }
//      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) { result =>
//        result shouldBe Future(gameOfThrones.as[DataModel])
//      }
//    }
//  }
}
