import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the Cellar entity.
 */
class CellarGatlingTest extends Simulation {
    def isJsonResponse(response: Response): Boolean = response.header(HttpHeaderNames.ContentType).exists(_.contains(HttpHeaderValues.ApplicationJson))

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the Cellar entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
        .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(1)
        .exec(http("Authenticated request")
            .get("/api/account")
            .headers(headers_http_authenticated)
            .check(status.is(200), jsonPath("$.id").saveAs("user_id"))
        )
        .pause(10)
        .repeat(2) {
            exec(http("Get all cellars")
                .get("/api/cellars")
                .headers(headers_http_authenticated)
                .check(status.is(200), jsonPath("$..id").saveAs("cellar_id"))
            )
            .pause(10 seconds, 20 seconds)
        }
        .repeat(5) {
            exec(http("Get created cellar")
            .get("/api/cellars/${cellar_id}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val preScn = scenario("Create required data for Cellar entity")
        .exec(http("Authentication")
            .post("/api/authenticate")
            .headers(headers_http_authentication)
            .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
            .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(1)
        .exec(http("Authenticated request")
            .get("/api/account")
            .headers(headers_http_authenticated)
            .check(status.is(200), jsonPath("$.id").saveAs("user_id"))
        )
        .pause(10)
        .exec(http("Get cellar for user")
            .get("/api/users/${user_id}/cellars")
            .headers(headers_http_authenticated)
            .check(status.saveAs("last_status"))
        )
        .doIfEquals("${last_status}", 200){
            exec(http("Create new cellar")
                .post("/api/cellars")
                .headers(headers_http_authenticated)
                .body(StringBody("""{"id":null, "capacity":"0", "userId":${user_id}}""")).asJSON
                .check(status.is(201))
            ).exitHereIfFailed
        }


    val users = scenario("Users").exec(scn)

    setUp(
        preScn.inject(atOnceUsers(1)),
        users.inject(rampUsers(Integer.getInteger("users", 100)) over (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
