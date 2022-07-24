package io.ccjmne;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.TemporalAdjusters.next;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.ccjmne.check_services.services.UserSupportService;
import io.ccjmne.config.UserSupportConfig;
import io.ccjmne.providers.ClockProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class UserSupportServiceTest {

  @Inject
  UserSupportService userSupport;

  @Inject
  UserSupportConfig config;

  @InjectMock
  ClockProvider clock;

  @Test
  public void userSupport_weekend() {
    fixClock(DayOfWeek.SATURDAY, LocalTime.MIDNIGHT);
    assertFalse(userSupport.isAvailable(), "User support shouldn't be available on weekends, in the middle of the night");

    fixClock(DayOfWeek.SATURDAY, LocalTime.NOON);
    assertFalse(userSupport.isAvailable(), "User support shouldn't be available on weekends, even in the middle of the day");
  }

  @Test
  public void userSupport_weekday() {
    fixClock(DayOfWeek.MONDAY, config.workhours().from().minus(1, SECONDS));
    assertFalse(
      userSupport.isAvailable(),
      String.format("User support shouldn't be available on weekdays before %s", config.workhours().from())
    );

    fixClock(DayOfWeek.MONDAY, config.workhours().from());
    assertTrue(
      userSupport.isAvailable(),
      String.format("User support should be available on weekdays from %s", config.workhours().from())
    );

    fixClock(DayOfWeek.MONDAY, config.workhours().to());
    assertTrue(
      userSupport.isAvailable(),
      String.format("User support should be available on weekdays until %s", config.workhours().to())
    );

    fixClock(DayOfWeek.MONDAY, config.workhours().to().plus(1, SECONDS));
    assertFalse(
      userSupport.isAvailable(),
      String.format("User support shouldn't be available on weekdays after %s", config.workhours().to())
    );
  }

  private void fixClock(final DayOfWeek day, final LocalTime time) {
    Mockito.when(clock.get())
      .thenReturn(Clock.fixed(now().with(next(day)).with(time).atZone(config.timezone()).toInstant(), config.timezone()));
  }

}
