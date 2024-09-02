package models

import play.api.data._
import play.api.data.Forms._
import play.api.http.Writeable
import play.api.libs.json.{JsObject, JsValue, Json, OFormat, OWrites, Writes}

case class DataModel(_id: String, name: String, description: String = "", pageCount: Int, thumbnail: String = "")

object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]

//  val dataModelForm: Form[DataModel] = Form(
//    mapping(
//      "_id" -> text,
//      "name" -> text,
//      "description" -> text,
//      "pageCount" -> number,
//      "thumbnail" -> text
//    )(DataModel.apply)(DataModel.unapply)
//  )
}


case class ApiDataModel(id: String, volumeInfo: VolumeInfo)

object ApiDataModel {
  implicit val formats: OFormat[ApiDataModel] = Json.format[ApiDataModel]


}

case class VolumeInfo(title: Option[String], description: Option[String], pageCount: Option[Int], imageLinks: Option[ImageLinks])

object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}

case class ImageLinks(thumbnail: String)

object ImageLinks {
  implicit val formats: OFormat[ImageLinks] = Json.format[ImageLinks]
}


case class ResultList(items: Seq[ApiDataModel])

object ResultList {
  implicit val formats: OFormat[ResultList] = Json.format[ResultList]
  //implicit val writes: Writeable[ResultList] = Writeable[ResultList]
}








