package models

import play.api.libs.json.{Json, OFormat}

case class UpdateModel(title: String, entry: String)

object UpdateModel {
  implicit val formats: OFormat[UpdateModel] = Json.format[UpdateModel]
}