package io.ccjmne.check_services.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import com.neovisionaries.i18n.CountryCode;

import io.ccjmne.check_services.adpartner.AdPartnerEndpoint;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AdsService {

  @Inject
  @RestClient
  AdPartnerEndpoint adPartner;

  public Boolean isAvailableIn(final CountryCode country) {
    try {
      return adPartner.canProvideFor(country).ads.equalsIgnoreCase("Sure, why not!");
    } catch (final ClientWebApplicationException e) {
      Log.error("The advertisment partner committed seppuku", e);
      return false;
    }
  }

}
