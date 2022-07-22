package io.ccjmne.check_services;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.neovisionaries.i18n.CountryCode;

import io.ccjmne.responses.CheckServicesResponse;

@Path("/services")
public class CheckServicesEndpoint {

  @Inject
  UserSupportService userSupport;

  @GET
  @Produces(APPLICATION_JSON)
  public CheckServicesResponse get(
    @NotNull @NotEmpty @QueryParam("timezone") final String timezone,
    @NotNull @NotEmpty @QueryParam("userId") final String userId,
    @NotNull @QueryParam("cc") final CountryCode cc
  ) {
    return new CheckServicesResponse(false, userSupport.isAvailable(), false);
  }

}
