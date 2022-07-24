package io.ccjmne.users;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

@Path("/users")
@RolesAllowed("ADMIN")
public class UsersEndpoint {

  @Inject
  UsersRepository users;

  @GET
  public List<User> listUsers() {
    return users.listAll();
  }

  @GET
  @Path("/{id}")
  public User lookupUser(@PathParam("id") final ObjectId id) {
    return users.findByIdOptional(id)
      .orElseThrow(() -> new NotFoundException(String.format("no user with id: '%s'", id)));
  }

  @DELETE
  @Path("/{id}")
  public Response removeUser(@PathParam("id") final ObjectId id) {
    users.delete(lookupUser(id));
    return Response.noContent().build();
  }

}
