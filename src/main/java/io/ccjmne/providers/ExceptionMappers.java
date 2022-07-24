package io.ccjmne.providers;

import java.util.Collections;
import java.util.Map;

import javax.validation.ValidationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.Status;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ExceptionMappers {

  @ServerExceptionMapper
  public RestResponse<Map<String, String>> mapException(final ValidationException e) {
    return RestResponse.status(Status.BAD_REQUEST, entity(e));
  }

  @ServerExceptionMapper
  public RestResponse<Map<String, String>> mapException(final NotFoundException e) {
    return RestResponse.status(Status.NOT_FOUND, entity(e));
  }

  @ServerExceptionMapper
  public RestResponse<Map<String, String>> mapException(final InternalServerErrorException e) {
    return RestResponse.status(Status.INTERNAL_SERVER_ERROR, entity(e.getCause()));
  }

  @ServerExceptionMapper
  public RestResponse<Map<String, String>> mapException(final BadRequestException e) {
    return RestResponse.status(Status.BAD_REQUEST, entity(e));
  }

  private static Map<String, String> entity(final Throwable e) {
    return Collections.singletonMap("error", e.getMessage());
  }

}
