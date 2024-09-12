package controllers

import com.google.inject.Singleton
import models.{APIError, DataModel, DatabaseError, UpdateModel}
import org.mongodb.scala.result
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Result}
import repositories.DataRepository
import services.{LibraryService, RepositoryService}
import views.html.helper

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import scala.reflect.runtime.universe.Throw


@Singleton
class ApplicationController @Inject()(
                                       repositoryService: RepositoryService,
                                       libraryService: LibraryService,
                                     )(implicit ec: ExecutionContext, val controllerComponents: ControllerComponents) extends BaseController {


  def index() = Action.async { implicit request =>
    repositoryService.index().map {
      case Right(item: Seq[DataModel]) => Ok {
        Json.toJson(item)
      }
      case Left(error: DatabaseError) => Status(error.ResponseStatus)(Json.toJson(error.reason))
    }
  }

  def readById(id: String) = Action.async { implicit request => {

    repositoryService.readById(id).map {
      case Right(item: Option[DataModel]) => Ok {
        Json.toJson(item)
      }
      case Left(error: DatabaseError) => Status(error.ResponseStatus)(Json.toJson(error.reason))
    }
  }
  }

  def readByName(name: String) = Action.async { implicit request => {
    repositoryService.readByName(name).map {
      case Right(item: Option[DataModel]) => Ok {
        Json.toJson(item)
      }
      case Left(error: DatabaseError) => Status(error.ResponseStatus)(Json.toJson(error.reason))
    }
  }
  }

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => repositoryService.create(dataModel).map {
        case Right(item: DataModel) => Created(Json.toJson(s"Added ${item.name} to database"))
        case Left(error: DatabaseError) => Status(error.ResponseStatus)(Json.toJson(error.reason))
      }
      case JsError(_) => Future(BadRequest)
    }
  }

  def update(id: String) = Action.async(parse.json) { implicit request =>

    request.body.validate[UpdateModel] match {
      case JsSuccess(updateModel, _) => repositoryService.update(id,updateModel).map{
        case Right(true) => Accepted {
          Json.toJson(s"updated")
        }
        case Left(error: DatabaseError) => Status(error.ResponseStatus)(Json.toJson(error.reason))
      }
      case _ => Future(BadRequest)
    }
  }

  def delete(id: String) = Action.async { implicit request => {

    repositoryService.delete(id).map {
      case Right(true) => Accepted {
      Json.toJson(s"Deleted")
    }
    case Left(error: DatabaseError) => Status(error.ResponseStatus)(Json.toJson(error.reason))
    }

  }
  }

  def deleteAll() = Action.async { implicit request => {

    repositoryService.deleteAll.map { case Right(item) => Accepted {
      Json.toJson(s"Deleted")
    }
    case Left(error: DatabaseError) => Status(error.ResponseStatus)(Json.toJson(error.reason))
    }

  }
  }

  def getGoogleBook(term: String): Action[AnyContent] = Action.async { implicit request =>

    libraryService.getGoogleBook(term = term).value.map {
      case Right(books) =>
        Ok {
        Json.toJson(books)
      }
      case Left(error: APIError) =>
        Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

  def home(): Action[AnyContent] = Action.async { implicit request =>{
    Future.successful(Ok(views.html.home()))
  }
  }


  def search(search: String ): Action[AnyContent] = Action.async {implicit request =>{

    libraryService.getGoogleBook(term = search).value.map {
      case Right(books) =>

        Ok(views.html.results(search, books))
      case Left(error: APIError) =>
        Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
    //Future.successful(Ok(views.html.results(search, results: Future[Object])))
  }}

}










//
//@import models.SearchModel
//@import helper._
//
//@(searchForm : Form[SearchModel])(implicit request: RequestHeader, messages: Messages)
//
//  @main("Find Book"){
//
//    <div class="container h-100 d-flex justify-content-center ">
//
//      <div class="p-5 mb-4 bg-secondary-subtle rounded-3 ">
//        <div class="container-fluid py-5 ">
//          <h1 class="display-5 fw-bold text-center">Google Book Database</h1>
//          <div class="row justify-content-center">
//            <p class="col-md-8 fs-4 p-4 text-center ">Search for a book and save it to your bookmarks</p>
//          </div>
//          <form class="d-flex" role="search">
//
//            <input class="form-control me-2" id="search" type="search" value="Search" placeholder="Search" aria-label="Search" @toHtmlArgs(args)>
//
//              <button class="btn bg-secondary"  type="submit" >Search</button>
//            </form>
//          </div>
//        </div>
//
//      </div>
//      }
