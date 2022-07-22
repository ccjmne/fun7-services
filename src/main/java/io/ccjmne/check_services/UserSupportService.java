package io.ccjmne.check_services;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class UserSupportService {

  public static final LocalTime WORKDAY_START = LocalTime.of(9, 0);
  public static final LocalTime WORKDAY_END   = LocalTime.of(15, 0);

  @Inject
  Clock clock;

  public boolean isAvailable() {
    final LocalDateTime now = LocalDateTime.now(clock);

    return now.getDayOfWeek().compareTo(DayOfWeek.MONDAY) >= 0
      && now.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) <= 0
      && !now.isBefore(now.with(WORKDAY_START))
      && !now.isAfter(now.with(WORKDAY_END));
  }

}

@ApplicationScoped
class Producers {

  @Singleton
  @Produces
  final ZoneId TZ = ZoneId.of(ConfigProvider.getConfig().getValue("USER_SUPPORT_TZ", String.class));

  @ApplicationScoped
  @Produces
  final Clock clock = Clock.system(TZ);

}
