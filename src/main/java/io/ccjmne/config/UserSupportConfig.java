package io.ccjmne.config;

import java.time.LocalTime;
import java.time.ZoneId;

import org.eclipse.microprofile.config.spi.Converter;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.quarkus.runtime.configuration.ZoneIdConverter;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.ConfigMapping.NamingStrategy;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;

@StaticInitSafe
@ConfigMapping(prefix = "user_support", namingStrategy = NamingStrategy.KEBAB_CASE)
public interface UserSupportConfig {

  @WithDefault("Europe/Ljubljana")
  @WithConverter(ZoneIdConverter.class)
  public ZoneId timezone();

  public Workhours workhours();

  interface Workhours {

    @WithConverter(LocalTimeConverter.class)
    @WithDefault("9:00")
    public LocalTime from();

    @WithConverter(LocalTimeConverter.class)
    @WithDefault("15:00")
    public LocalTime to();

  }

  static class LocalTimeConverter implements Converter<LocalTime> {

    @Override
    public LocalTime convert(final String time) throws IllegalArgumentException, NullPointerException {
      return LocalTime.parse(time);
    }

  }

}
