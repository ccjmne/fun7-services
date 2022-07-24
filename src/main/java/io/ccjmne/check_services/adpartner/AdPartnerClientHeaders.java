package io.ccjmne.check_services.adpartner;

import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import io.ccjmne.config.AdPartnerConfig;

@ApplicationScoped
public class AdPartnerClientHeaders implements ClientHeadersFactory {

  @Inject
  AdPartnerConfig config;

  @Override
  public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incoming, MultivaluedMap<String, String> outgoing) {
    if (!config.username().isPresent()) {
      return outgoing;
    }

    final MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
    headers.putAll(outgoing);
    headers.putSingle(
      "Authorization",
      String.format("Basic %s", Base64.getEncoder().encodeToString(String.format("%s:%s", config.username(), config.password()).getBytes()))
    );

    return headers;
  }

}
