package controllers

import com.google.inject.Singleton
import models.DataModel
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Result}
import repositories.DataRepository

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject



@Singleton
class ApplicationController @Inject()(dataRepository: DataRepository)(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController{

//  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//    Future.successful(Ok(views.html.index()))
//  }

  def index() = Action.async { implicit request =>
    dataRepository.index().map{
      case Right(item: Seq[DataModel]) => Ok {Json.toJson(item)}
      case Left(error) => Status(error)(Json.toJson("Unable to find any books"))
    }

  }
  def read(id: String) = Action.async { implicit request => {
    dataRepository.read(id: String).map {
      case Right(item) => Ok {Json.toJson(item)}
      case Left(error) => Status(error)(Json.toJson("Unable to find any books"))
    }
  }
  }

  def create() = TODO

  def update(id: String) = TODO

  def delete(id: String) = TODO
}
