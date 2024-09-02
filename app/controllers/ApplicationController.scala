package controllers

import com.google.inject.Singleton
import models.DataModel
import org.mongodb.scala.result
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Result}
import repositories.DataRepository
import services.LibraryService

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import scala.reflect.runtime.universe.Throw


@Singleton
class ApplicationController @Inject()(dataRepository: DataRepository, libraryService: LibraryService)
                                     (implicit ec: ExecutionContext, val controllerComponents: ControllerComponents) extends BaseController {

  //  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
  //    Future.successful(Ok(views.html.index()))
  //  }

  def index() = Action.async { implicit request =>
    dataRepository.index().map {
      case Right(item: Seq[DataModel]) => Ok {
        Json.toJson(item)
      }
      case Left(error) => Status(404)(Json.toJson("Unable to find any books"))
    }
  }

  def readById(id: String) = Action.async { implicit request => {

    dataRepository.readById(id).map {
      case item: Option[DataModel] => Ok {
        Json.toJson(item)
      }
      case null => Status(404)(Json.toJson("Unable to find any books"))
    }
  }
  }

  def readByName(id: String) = Action.async { implicit request => {
    dataRepository.readByName(id).map {
      case item: Option[DataModel] => Ok {
        Json.toJson(item)
      }
      case null => Status(404)(Json.toJson("Unable to find any books"))
    }
  }
  }

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def update(id: String) = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => dataRepository.update(id, dataModel).map(_ => Accepted {
        Json.toJson(dataModel)
      })
      case JsError(_) => Future(BadRequest)
    }
  }

  def delete(id: String) = Action.async(parse.json) { implicit request => {
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => dataRepository.delete(id: String).map { dataModel => Accepted }
      case JsError(_) => Future(BadRequest)
    }
  }
  }

  def getGoogleBook(term: String): Action[AnyContent] = Action.async { implicit request =>
    libraryService.getGoogleBook(term = term).value.map {
      case Right(book) => Ok {
        Json.toJson(book.combined)
      }
      case Left(error) => Status(404)(Json.toJson("Unable to find any books"))
    }

  }

}


