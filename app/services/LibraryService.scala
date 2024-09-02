package services

import cats.data.EitherT
import connectors.LibraryConnector
import models._

import java.awt.print.Book
import javax.inject.Inject
import com.google.inject.Singleton

import scala.concurrent.{ExecutionContext, Future}
//https://www.googleapis.com/books/v1/volumes?q=$term+intitle

@Singleton
case class LibraryService @Inject()(connector: LibraryConnector) {

  def getGoogleBook(urlOverride: Option[String] = None, term: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, ResultList] =
    connector.get[ResultList](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$term:intitle"))
}


