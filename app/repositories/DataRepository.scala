package repositories

import models.{APIError, DataModel, DatabaseError}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import org.mongodb.scala._
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import play.api.http.Status.{BAD_REQUEST, NOT_FOUND}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


/**
 * This section creates a new DataRepository class and injects dependencies into it required for every Mongo Repository.
 * All of the return types of these functions are async futures
 *
 * @param mongoComponent mongo component
 * @param ec             execute program logic asynchronously
 */
@Singleton
class DataRepository @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "DataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(Indexes.ascending("_id"))),
  replaceIndexes = false
) {
  def index(): Future[Either[DatabaseError.BadAPIResponse, Seq[DataModel]]] = //list of all the dataModels in the database
    collection.find().toFuture().map {
      books: Seq[DataModel] => Right(books)
    }.recover { case _ =>
      Left(DatabaseError.BadAPIResponse(NOT_FOUND, "Books can't be found"))
    }


  def create(book: DataModel): Future[Either[DatabaseError.BadAPIResponse, DataModel]] =
    collection.insertOne(book).toFuture().map {
      _ => Right(book)
    }.recover { case _ =>
      Left(DatabaseError.BadAPIResponse(500, "Could not create"))
    }

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  private def byName(name: String): Bson =
    Filters.and(
      Filters.equal("name", name)
    )


  def readById(id: String): Future[Either[DatabaseError.BadAPIResponse, Option[DataModel]]] = {
    collection.find(byID(id)).headOption.flatMap {
      case Some(book) =>
        Future(Right(Some(book)))
      case _ =>
        Future(Left(DatabaseError.BadAPIResponse(500, "Could not find Book")))
    }
  }

  def readByName(name: String): Future[Either[DatabaseError.BadAPIResponse, Option[DataModel]]] = {
    collection.find(byName(name)).headOption.flatMap {
      case Some(book) =>
        Future(Right(Some(book)))
      case _ =>
        Future(Left(DatabaseError.BadAPIResponse(500, "Could not find Book")))
    }
  }


  def update(id: String, entry: Map[String, String]): Future[Either[DatabaseError.BadAPIResponse, result.UpdateResult]] = {
    val updateDocument = Document("$set" -> Document(entry))

    collection.updateOne(Filters.equal("_id", id), updateDocument)
      .toFuture().map {
        book => Right(book)
      }.recover { case _ =>
        Left(DatabaseError.BadAPIResponse(500, "Could not update"))
      }
  }

  def delete(id: String): Future[Either[DatabaseError.BadAPIResponse, result.DeleteResult]] =
    collection.deleteOne(
      filter = byID(id)
    ).toFuture().map {
      book => Right {
        book
      }
    }.recover { case _ =>
      Left(DatabaseError.BadAPIResponse(500, "Could not delete"))
    }


  def deleteAll(): Future[Either[DatabaseError.BadAPIResponse, result.DeleteResult]] =
    collection.deleteMany(
      empty()).toFuture().map{
      book => Right {
        book
      }
    }.recover { case _ =>
    Left(DatabaseError.BadAPIResponse(500, "Could not delete all"))
  }


}


