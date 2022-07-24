package io.ccjmne.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.ConfigMapping.NamingStrategy;

@StaticInitSafe
@ConfigMapping(prefix = "adpartner", namingStrategy = NamingStrategy.KEBAB_CASE)
public interface AdPartnerConfig {

  public String username();

  public String password();

}
