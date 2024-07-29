package models

import play.api.libs.json.{JsObject, Json, OFormat}

case class ResponseItem(id: String, volumeInfo: JsObject)  {

}

case class VolumeInfo(title: String, description: Option[String], pageCount: Int)  {

}

case class DataModel(id: String, volumeInfo: JsObject ){

}


object DataModel{

  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}



object VolumeInfo{
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]

}

object ResponseItem{

  implicit val formats: OFormat[ResponseItem] = Json.format[ResponseItem]


}
