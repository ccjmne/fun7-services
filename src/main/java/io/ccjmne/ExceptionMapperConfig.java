package io.ccjmne;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperConfig implements ExceptionMapper<ValidationException> {

  @Override
  public Response toResponse(final ValidationException e) {
    return Response.status(BAD_REQUEST).entity(e.getMessage()).type(TEXT_PLAIN).build();
  }

}
