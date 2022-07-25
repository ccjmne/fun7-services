package io.ccjmne.check_services;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.bson.types.ObjectId;

import com.neovisionaries.i18n.CountryCode;

import io.ccjmne.check_services.services.AdsService;
import io.ccjmne.check_services.services.MultiplayerService;
import io.ccjmne.check_services.services.UserSupportService;
import io.ccjmne.users.User;
import io.ccjmne.users.UsersRepository;

@Path("/")
public class CheckServicesEndpoint {

  @Inject
  UserSupportService userSupport;

  @Inject
  MultiplayerService multiplayer;

  @Inject
  AdsService ads;

  @Inject
  UsersRepository users;

  @GET
  public CheckServicesResponse get(
    @QueryParam("timezone") final String timezone,
    @QueryParam("userId") @NotNull final ObjectId userId,
    @QueryParam("cc") @NotNull final CountryCode cc
  ) {
    final User user = users.findByIdOptional(userId).orElse(new User(userId)).checkedInFrom(cc);
    users.persistOrUpdate(user);
    return new CheckServicesResponse(multiplayer.isAvailableTo(user), userSupport.isAvailable(), ads.isAvailableIn(cc));
  }

}
