package services

import models.{DataModel, DatabaseError, UpdateModel}
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.Future

class RepositoryService @Inject()(dataRepository: DataRepository){

  def index(): Future[Either[DatabaseError.BadAPIResponse, Seq[DataModel]]] = {
    dataRepository.index()
  }

  def readById(id:String): Future[Either[DatabaseError.BadAPIResponse, Option[DataModel]]] = {
    dataRepository.readById(id)
  }

  def readByName(id:String): Future[Either[DatabaseError.BadAPIResponse, Option[DataModel]]] = {
    dataRepository.readByName(id)
  }

  def create(dataModel: DataModel): Future[Either[DatabaseError.BadAPIResponse, DataModel]] = {
    dataRepository.create(dataModel)
  }

  def update(id: String, entry: UpdateModel): Future[Either[DatabaseError.BadAPIResponse, UpdateResult]] = {

    dataRepository.update(id, Map(entry.title -> entry.entry))
  }

  def delete(id:String): Future[Either[DatabaseError.BadAPIResponse, DeleteResult]] = {
    dataRepository.delete(id)
  }

  def deleteAll: Future[Either[DatabaseError.BadAPIResponse, DeleteResult]] = {
    dataRepository.deleteAll()
  }

}
