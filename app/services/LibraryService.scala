package services

import connectors.LibraryConnector
import models.{DataModel, ResponseItem, VolumeInfo}

import java.awt.print.Book
import javax.inject.Inject
import com.google.inject.Singleton

import scala.concurrent.{ExecutionContext, Future}


@Singleton
case class LibraryService @Inject()(connector: LibraryConnector) {

  def getGoogleBook(urlOverride: Option[String] = None, term: String)(implicit ec: ExecutionContext): Future[DataModel] =
    connector.get[DataModel](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes/$term"))

}

//case class fetch @Inject()(libraryService: LibraryService)(search: String, term: String)(implicit ec: ExecutionContext){
//
//  def fetchBooks = libraryService.getGoogleBook(search = "flowers", term = "inauthor")
//
//}
