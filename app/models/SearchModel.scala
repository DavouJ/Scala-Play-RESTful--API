package models

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, text}
import play.api.libs.json.Reads.required

case class SearchModel(search: String) {}

object SearchModel{
  val searchForm: Form[SearchModel] = Form(
    mapping(
      "search" -> nonEmptyText
    )(SearchModel.apply)(SearchModel.unapply)
  )
}