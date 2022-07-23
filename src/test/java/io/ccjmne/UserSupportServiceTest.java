package io.ccjmne;

import static io.ccjmne.check_services.services.UserSupportService.WORKDAY_END;
import static io.ccjmne.check_services.services.UserSupportService.WORKDAY_START;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.ccjmne.check_services.services.UserSupportService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class UserSupportServiceTest {

  @Inject
  UserSupportService userSupport;

  @Inject
  ZoneId TZ;

  @InjectMock
  Clock clock;

  @Test
  public void userSupport_weekend() {
    fixClock(DayOfWeek.SATURDAY, LocalTime.MIDNIGHT);
    assertFalse(userSupport.isAvailable(), "User support shouldn't be available on weekends, in the middle of the night");

    fixClock(DayOfWeek.SATURDAY, LocalTime.NOON);
    assertFalse(userSupport.isAvailable(), "User support shouldn't be available on weekends, even in the middle of the day");
  }

  @Test
  public void userSupport_weekday() {
    fixClock(DayOfWeek.MONDAY, WORKDAY_START.minus(1, SECONDS));
    assertFalse(userSupport.isAvailable(), "User support shouldn't be available on weekdays before 0900");

    fixClock(DayOfWeek.MONDAY, WORKDAY_START);
    assertTrue(userSupport.isAvailable(), "User support should be available on weekdays from 0900");

    fixClock(DayOfWeek.MONDAY, WORKDAY_END);
    assertTrue(userSupport.isAvailable(), "User support should be available on weekdays until 1500");

    fixClock(DayOfWeek.MONDAY, WORKDAY_END.plus(1, SECONDS));
    assertFalse(userSupport.isAvailable(), "User support shouldn't be available on weekdays after 1500");
  }

  private void fixClock(final DayOfWeek day, final LocalTime time) {
    Mockito.when(clock.instant()).thenReturn(LocalDateTime.now().atZone(TZ).with(TemporalAdjusters.nextOrSame(day)).with(time).toInstant());
    Mockito.when(clock.getZone()).thenReturn(TZ);
  }

}
