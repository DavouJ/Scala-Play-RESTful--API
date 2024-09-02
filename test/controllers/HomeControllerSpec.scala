package controllers

import baseSpec.BaseSpec
import org.jsoup.Jsoup
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.ControllerComponents
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 *
 * Guice - spins up an application then runs test on said application
 */
class HomeControllerSpec extends BaseSpec with Injecting with GuiceOneAppPerSuite {

  val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController(controllerComponents)
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Play Scala API Demo")
    }

    "render the index page with the expected header" in {
      val controller = inject[HomeController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      Jsoup.parse(contentAsString(home)).getElementById("header").text() shouldBe("Play Scala API Demo")
    }

    "render the index with the expected title" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      Jsoup.parse(contentAsString(home)).title() shouldBe("Welcome to Play")
    }
  }
}
