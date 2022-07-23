package io.ccjmne.providers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import com.neovisionaries.i18n.CountryCode;

@Provider
public class ParameterConverters implements ParamConverterProvider {

  @Override
  @SuppressWarnings("unchecked")
  public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
    if (rawType.isAssignableFrom(CountryCode.class)) {
      return (ParamConverter<T>) new CountryCodeConverter();
    }

    return null;
  }

}

class CountryCodeConverter implements ParamConverter<CountryCode> {

  @Override
  public CountryCode fromString(final String code) {
    return Optional.ofNullable(CountryCode.getByCodeIgnoreCase(code))
      .orElseThrow(() -> new BadRequestException(String.format("invalid country code: '%s', should use ISO 3166-1 alpha-2 or -3", code)));
  }

  @Override
  public String toString(final CountryCode country) {
    return country.getAlpha2();
  }

}
