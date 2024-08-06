package services

import cats.data.EitherT
import connectors.LibraryConnector
import models.{APIError, ApiDataModel, DataModel}

import java.awt.print.Book
import javax.inject.Inject
import com.google.inject.Singleton

import scala.concurrent.{ExecutionContext, Future}


@Singleton
case class LibraryService @Inject()(connector: LibraryConnector) {

  def getGoogleBook(urlOverride: Option[String] = None, term: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, ApiDataModel] =
    connector.get[ApiDataModel](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes/$term"))

}


