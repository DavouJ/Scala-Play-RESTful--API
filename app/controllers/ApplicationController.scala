package controllers

import com.google.inject.Singleton
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Result}
import scala.concurrent.Future
import javax.inject.Inject



@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController{

//  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//    Future.successful(Ok(views.html.index()))
//  }

  def index() = Action {
    Ok("yo").withHeaders(ETAG -> "0")
  }

  def create() = TODO

  def read(id: String): Action[AnyContent] = TODO

  def update(id: String) = TODO

  def delete(id: String) = TODO
}
