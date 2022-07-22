package io.ccjmne;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.ccjmne.check_services.CheckServicesEndpoint;
import io.ccjmne.check_services.UserSupportService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.specification.RequestSpecification;

@QuarkusTest
public class CheckServicesTest {

  @com.google.inject.Inject
  @TestHTTPEndpoint(CheckServicesEndpoint.class)
  @TestHTTPResource
  URL services;

  @InjectMock
  UserSupportService userSupport;

  @Test
  public void missingParams() {
    given()
      .when().get(services)
      .then()
      .statusCode(BAD_REQUEST.getStatusCode())
      .contentType(TEXT_PLAIN)
      .body(containsString("must not be null"));
  }

  @Test
  public void invalidCC() {
    given()
      .queryParam("cc", 250)
      .when().get(services)
      .then()
      .statusCode(BAD_REQUEST.getStatusCode())
      .contentType(TEXT_PLAIN)
      .body(startsWith("Invalid country code"));
  }

  @Test
  public void userSupport_enabled() {
    Mockito.when(userSupport.isAvailable()).thenReturn(true);

    givenValidParams()
      .when().get(services)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("\"user-support\":\"enabled\""));
  }

  @Test
  public void userSupport_disabled() {
    Mockito.when(userSupport.isAvailable()).thenReturn(false);

    givenValidParams()
      .when().get(services)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("\"user-support\":\"disabled\""));
  }

  private RequestSpecification givenValidParams() {
    return given()
      .queryParam("timezone", "Europe/Ljubljana")
      .queryParam("userId", "0")
      .queryParam("cc", "si");
  }

}
