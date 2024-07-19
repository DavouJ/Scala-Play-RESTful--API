package repositories

import models.DataModel
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


/**
 * This section creates a new DataRepository class and injects dependencies into it required for every Mongo Repository.
 * All of the return types of these functions are async futures
 * @param mongoComponent mongo component
 * @param ec execute program logic asynchronously
 */
@Singleton
class DataRepository @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "DataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(Indexes.ascending("_id"))),
  replaceIndexes = false
) {
  def index(): Future[Either[Int, Seq[DataModel]]] =    //list of all the dataModels in the database
    collection.find().toFuture().map {
      case books: Seq[DataModel] => Right(books)
      case _ => Left(404)
    }
  def create()(book: DataModel): Future[DataModel] =
    collection.insertOne(book).toFuture().map(_ => book)

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def read(id: String): Future[Either[Int, DataModel]] =
    collection.find(byID(id)).headOption flatMap {
      case Some(data) =>
        Future(Right(data))
      case _ => Future(Left(404))
    }

  def update(id: String, book: DataModel): Future[result.UpdateResult] =
    collection.replaceOne(
      filter = byID(id),
      replacement = book,
      options = new ReplaceOptions().upsert(true)).toFuture() /// LOOK into

  def delete(id:String): Future[result.DeleteResult] =
    collection.deleteOne(
      filter = byID(id)).toFuture()

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ())



}


