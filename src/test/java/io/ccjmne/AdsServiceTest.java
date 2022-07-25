package io.ccjmne;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.neovisionaries.i18n.CountryCode;

import io.ccjmne.endpoints.services.adpartner.AdPartnerClient;
import io.ccjmne.endpoints.services.adpartner.AdPartnerResponse;
import io.ccjmne.endpoints.services.impl.AdsService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class AdsServiceTest {

  @Inject
  AdsService ads;

  @InjectMock
  @RestClient
  AdPartnerClient adPartner;

  @Test
  public void ads_unauthorized() {
    Mockito.when(adPartner.canProvideFor(any(CountryCode.class)))
      .thenThrow(new WebApplicationException("Invalid user credentials", UNAUTHORIZED));
    assertThrows(WebApplicationException.class, () -> ads.isAvailableIn(CountryCode.SI));
  }

  @Test
  public void ads_available() {
    Mockito.when(adPartner.canProvideFor(any(CountryCode.class)))
      .thenReturn(new AdPartnerResponse(AdPartnerResponse.YES));
    assertTrue(ads.isAvailableIn(CountryCode.SI));
  }

  @Test
  public void ads_unavailable() {
    Mockito.when(adPartner.canProvideFor(any(CountryCode.class)))
      .thenReturn(new AdPartnerResponse(AdPartnerResponse.NO));
    assertFalse(ads.isAvailableIn(CountryCode.SI));
  }

  @Test
  public void ads_seppuku() {
    Mockito.when(adPartner.canProvideFor(any(CountryCode.class)))
      .thenThrow(new WebApplicationException("Request handler committed seppuku!", INTERNAL_SERVER_ERROR));
    assertFalse(ads.isAvailableIn(CountryCode.SI));
  }

}
