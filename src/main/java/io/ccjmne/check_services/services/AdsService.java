package io.ccjmne.check_services.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status.Family;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.neovisionaries.i18n.CountryCode;

import io.ccjmne.check_services.adpartner.AdPartnerClient;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AdsService {

  @Inject
  @RestClient
  AdPartnerClient adPartner;

  public Boolean isAvailableIn(final CountryCode country) {
    try {
      return adPartner.canProvideFor(country).isYes();
    } catch (final WebApplicationException e) {
      if (Family.SERVER_ERROR == e.getResponse().getStatusInfo().getFamily()) {
        Log.warn("The advertisment partner committed seppuku");
        return false;
      }

      Log.error(e);
      throw new InternalServerErrorException(e);
    }
  }

}
