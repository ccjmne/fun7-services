package io.ccjmne.endpoints.services.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.ccjmne.config.MultiplayerConfig;
import io.ccjmne.endpoints.users.User;

@ApplicationScoped
public class MultiplayerService {

  @Inject
  MultiplayerConfig config;

  public boolean isAvailableTo(final User user) {
    return user.country == config.country() && user.apiCalls >= config.minCheckins();
  }

}
