package io.ccjmne;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;

import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.ccjmne.config.EmbeddedUsersConfig;
import io.ccjmne.endpoints.users.User;
import io.ccjmne.endpoints.users.UsersEndpoint;
import io.ccjmne.endpoints.users.UsersRepository;
import io.quarkus.runtime.configuration.ConfigurationException;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.common.mapper.TypeRef;

@QuarkusTest
public class UsersEndpointTest {

  @TestHTTPEndpoint(UsersEndpoint.class)
  @TestHTTPResource
  URL users;

  @InjectMock
  UsersRepository repository;

  @Inject
  EmbeddedUsersConfig security;

  @Test
  public void unauthorized() {
    given()
      .when().get(users)
      .then()
      .statusCode(UNAUTHORIZED.getStatusCode());

    given()
      .when().get(users + "/123456789012345678901234")
      .then()
      .statusCode(UNAUTHORIZED.getStatusCode());

    given()
      .when().delete(users + "/123456789012345678901234")
      .then()
      .statusCode(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void listUsers() {
    Mockito.when(repository.listAll()).thenReturn(Collections.emptyList());
    given()
      .header("Authorization", getAuth())
      .when().get(users)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(is("[]"));

    final List<User> list = Arrays
      .asList(new User(new ObjectId("123456789012345678901234")), new User(new ObjectId("12345678901234567890abcd")));
    Mockito.when(repository.listAll()).thenReturn(list);

    assertArrayEquals(
      list.stream().map(user -> user.id.toHexString()).toArray(),
      given()
        .header("Authorization", getAuth())
        .when().get(users)
        .then()
        .statusCode(OK.getStatusCode())
        .contentType(APPLICATION_JSON)
        .extract().as(new TypeRef<List<User>>() {}).stream().map(user -> user.id.toHexString()).toArray()
    );
  }

  @Test
  public void lookupUser_invalidUserId() {
    given()
      .header("Authorization", getAuth())
      .when().get(users + "/eric")
      .then()
      .statusCode(BAD_REQUEST.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("invalid user id"));
  }

  @Test
  public void lookupUser_notFound() {
    Mockito.when(repository.findByIdOptional(any(ObjectId.class))).thenReturn(Optional.empty());
    given()
      .header("Authorization", getAuth())
      .when().get(users + "/123456789012345678901234")
      .then()
      .statusCode(NOT_FOUND.getStatusCode());
  }

  @Test
  public void lookupUser_found() {
    final String id = "123456789012345678901234";
    final User user = new User(new ObjectId(id));
    Mockito.when(repository.findByIdOptional(any(ObjectId.class))).thenReturn(Optional.of(user));
    given()
      .header("Authorization", getAuth())
      .when().get(users + "/" + id)
      .then()
      .statusCode(OK.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("\"id\":\"" + id + "\""));
  }

  @Test
  public void deleteUser_invalidUserId() {
    given()
      .header("Authorization", getAuth())
      .when().delete(users + "/eric")
      .then()
      .statusCode(BAD_REQUEST.getStatusCode())
      .contentType(APPLICATION_JSON)
      .body(containsString("invalid user id"));
  }

  @Test
  public void deleteUser_notFound() {
    Mockito.when(repository.findByIdOptional(any(ObjectId.class))).thenReturn(Optional.empty());
    given()
      .header("Authorization", getAuth())
      .when().delete(users + "/123456789012345678901234")
      .then()
      .statusCode(NOT_FOUND.getStatusCode());
  }

  @Test
  public void deleteUser_found() {
    final String id = "123456789012345678901234";
    final User user = new User(new ObjectId(id));
    Mockito.when(repository.findByIdOptional(any(ObjectId.class))).thenReturn(Optional.of(user));
    given()
      .header("Authorization", getAuth())
      .when().delete(users + "/" + id)
      .then()
      .statusCode(NO_CONTENT.getStatusCode());
  }

  private String getAuth() {
    final String username = security.roles().entrySet().stream().filter(s -> s.getValue().equals("ADMIN")).findAny()
      .orElseThrow(() -> new ConfigurationException("no embedded user for the ADMIN role")).getKey();
    final String password = Optional.ofNullable(security.users().get(username))
      .orElseThrow(() -> new ConfigurationException(String.format("no password set for user %s", username)));

    return String.format("Basic %s", Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes()));
  }

}
