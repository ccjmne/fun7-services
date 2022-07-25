package io.ccjmne.endpoints.services.adpartner;

import java.util.Base64;
import java.util.Collections;

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
    return new MultivaluedHashMap<>(
      config.username().isPresent()
        ? Collections.singletonMap(
          "Authorization", String.format(
            "Basic %s",
            Base64.getEncoder().encodeToString(String.format("%s:%s", config.username().get(), config.password().get()).getBytes())
          )
        )
        : Collections.emptyMap()
    );
  }

}
