package io.ccjmne.endpoints.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.ccjmne.config.UserSupportConfig;
import io.ccjmne.providers.ClockProvider;

@ApplicationScoped
public class UserSupportService {

  @Inject
  UserSupportConfig config;

  @Inject
  ClockProvider clock;

  public boolean isAvailable() {
    final LocalDateTime now = LocalDateTime.now(clock.get());

    return now.getDayOfWeek().compareTo(DayOfWeek.MONDAY) >= 0
      && now.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) <= 0
      && !now.isBefore(now.with(config.workhours().from()))
      && !now.isAfter(now.with(config.workhours().to()));
  }

}
