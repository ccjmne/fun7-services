package io.ccjmne;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import io.ccjmne.config.MultiplayerConfig;
import io.ccjmne.endpoints.services.impl.MultiplayerService;
import io.ccjmne.endpoints.users.User;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MultiplayerServiceTest {

  @Inject
  MultiplayerService multiplayer;

  @Inject
  MultiplayerConfig config;

  @Test
  public void multiplayer_ok() {
    assertTrue(multiplayer.isAvailableTo(multiplayerUser()));
  }

  @Test
  public void multiplayer_otherRegion() {
    final User user = multiplayerUser();
    user.country = config.country() == CountryCode.US ? CountryCode.NZ : CountryCode.US;

    assertFalse(multiplayer.isAvailableTo(user));
  }

  @Test
  public void multiplayer_novice() {
    final User user = multiplayerUser();
    user.apiCalls--;

    assertFalse(multiplayer.isAvailableTo(user));
  }

  private User multiplayerUser() {
    return new User(new ObjectId("123456789012345678901234"), config.country(), config.minCheckins());
  }

}
