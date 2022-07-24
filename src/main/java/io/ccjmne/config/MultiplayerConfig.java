package io.ccjmne.config;

import org.eclipse.microprofile.config.spi.Converter;

import com.neovisionaries.i18n.CountryCode;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.ConfigMapping.NamingStrategy;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;

@StaticInitSafe
@ConfigMapping(prefix = "multiplayer", namingStrategy = NamingStrategy.KEBAB_CASE)
public interface MultiplayerConfig {

  @WithConverter(CountryCodeConverter.class)
  @WithDefault("US")
  public CountryCode country();

  @WithDefault("5")
  public Integer minCheckins();

  static class CountryCodeConverter implements Converter<CountryCode> {

    @Override
    public CountryCode convert(final String value) throws IllegalArgumentException, NullPointerException {
      return CountryCode.getByCodeIgnoreCase(value);
    }

  }

}
