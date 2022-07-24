package io.ccjmne.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.ConfigMapping.NamingStrategy;

@StaticInitSafe
@ConfigMapping(prefix = "adpartner", namingStrategy = NamingStrategy.KEBAB_CASE)
public interface AdPartnerConfig {

  public Optional<String> username();

  public Optional<String> password();

}
