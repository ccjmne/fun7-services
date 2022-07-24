package io.ccjmne.check_services.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.ccjmne.config.MultiplayerConfig;
import io.ccjmne.users.User;

@ApplicationScoped
public class MultiplayerService {

  @Inject
  MultiplayerConfig config;

  public boolean isAvailableTo(final User user) {
    return user.country == config.country() && user.apiCalls >= config.minCheckins();
  }

}
