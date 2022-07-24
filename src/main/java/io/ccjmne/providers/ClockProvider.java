package io.ccjmne.providers;

import java.time.Clock;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.ccjmne.config.UserSupportConfig;

@ApplicationScoped
public class ClockProvider {

  @Inject
  UserSupportConfig config;

  public Clock get() {
    return Clock.system(config.timezone());
  }

}
