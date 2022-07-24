package io.ccjmne.check_services.adpartner;

import java.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.neovisionaries.i18n.CountryCode;

@RegisterRestClient(configKey = "adpartner")
@ClientHeaderParam(name = "Authorization", value = "{authorization}")
// TODO: somehow, using this just doesn't work. Needs investigating.
// @RegisterClientHeaders(AdPartnerClientHeaders.class)
public interface AdPartnerClient {

  @GET
  AdPartnerResponse canProvideFor(@QueryParam("countryCode") final CountryCode countryCode);

  String username = ConfigProvider.getConfig().getValue("adpartner.username", String.class);
  String password = ConfigProvider.getConfig().getValue("adpartner.password", String.class);

  String auth = String
    .format("Basic %s", Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes()));

  default String authorization() {
    return auth;
  }

}
