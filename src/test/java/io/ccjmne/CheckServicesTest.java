package io.ccjmne;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.Inject;
import com.neovisionaries.i18n.CountryCode;

import io.ccjmne.check_services.CheckServicesEndpoint;
import io.ccjmne.check_services.services.AdsService;
import io.ccjmne.check_services.services.MultiplayerService;
import io.ccjmne.check_services.services.UserSupportService;
import io.ccjmne.users.User;
import io.ccjmne.users.UsersRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.specification.RequestSpecification;

@QuarkusTest
public class CheckServicesTest {

  @TestHTTPEndpoint(CheckServicesEndpoint.class)
  @TestHTTPResource
  URL services;

  @InjectMock
  UserSupportService userSupport;

  @InjectMock
  MultiplayerService multiplayer;

  @InjectMock
  AdsService ads;

  @Inject
  UsersRepository users;

  @Test
  public void missingParams() {
    given()
      .when().get(services)
      .then()
      .statusCode(BAD_REQUEST.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("must not be null"));
  }

  @Test
  public void invalidCC() {
    given()
      .queryParam("cc", 250)
      .when().get(services)
      .then()
      .statusCode(BAD_REQUEST.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("invalid country code"));
  }

  @Test
  public void invalidUserId() {
    given()
      .queryParam("userId", "eric")
      .when().get(services)
      .then()
      .statusCode(BAD_REQUEST.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("invalid user id"));
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

  @Test
  public void multiplayer_enabled() {
    Mockito.when(multiplayer.isAvailableTo(any(User.class))).thenReturn(true);

    givenValidParams()
      .when().get(services)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("\"multiplayer\":\"enabled\""));
  }

  @Test
  public void multiplayer_disabled() {
    Mockito.when(multiplayer.isAvailableTo(any(User.class))).thenReturn(false);

    givenValidParams()
      .when().get(services)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("\"multiplayer\":\"disabled\""));
  }

  @Test
  public void ads_enabled() {
    Mockito.when(ads.isAvailableIn(any(CountryCode.class))).thenReturn(true);

    givenValidParams()
      .when().get(services)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("\"ads\":\"enabled\""));
  }

  @Test
  public void ads_disabled() {
    Mockito.when(ads.isAvailableIn(any(CountryCode.class))).thenReturn(false);

    givenValidParams()
      .when().get(services)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("\"ads\":\"disabled\""));
  }

  private RequestSpecification givenValidParams() {
    return given()
      .queryParam("timezone", "Europe/Ljubljana")
      .queryParam("userId", "123456789012345678901234")
      .queryParam("cc", "si");
  }

}
