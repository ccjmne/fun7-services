package io.ccjmne.config;

import java.util.Map;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.ConfigMapping.NamingStrategy;

@StaticInitSafe
@ConfigMapping(prefix = "quarkus.security.users.embedded", namingStrategy = NamingStrategy.KEBAB_CASE)
public interface EmbeddedUsersConfig {

  public boolean enabled();

  public boolean plainText();

  public Map<String, String> users();

  public Map<String, String> roles();

}
