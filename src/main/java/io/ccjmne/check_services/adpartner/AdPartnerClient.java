package io.ccjmne.check_services.adpartner;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.neovisionaries.i18n.CountryCode;

@RegisterRestClient(configKey = "adpartner")
@RegisterClientHeaders(AdPartnerClientHeaders.class)
public interface AdPartnerClient {

  @GET
  AdPartnerResponse canProvideFor(@QueryParam("countryCode") final CountryCode countryCode);

}
