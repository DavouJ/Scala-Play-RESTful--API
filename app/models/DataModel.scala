package models

import play.api.libs.json.{JsObject, JsValue, Json, OFormat, OWrites}

case class DataModel(id: String, volumeInfo: VolumeInfo) {
  val combined = Combined(id, volumeInfo.title, volumeInfo.description, volumeInfo.pageCount)
}

case class VolumeInfo(  title: String, description: Option[String], pageCount: Int)

case class Combined(id: String, title: String, description: Option[String], pageCount: Int)


object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}

object Combined {
  implicit val formats: OFormat[Combined] = Json.format[Combined]
}

object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]

}


