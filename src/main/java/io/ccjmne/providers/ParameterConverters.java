package io.ccjmne.providers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.bson.types.ObjectId;

import com.neovisionaries.i18n.CountryCode;

@Provider
public class ParameterConverters implements ParamConverterProvider {

  @Override
  @SuppressWarnings("unchecked")
  public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
    if (rawType.isAssignableFrom(CountryCode.class)) {
      return (ParamConverter<T>) new CountryCodeConverter();
    }

    if (rawType.isAssignableFrom(ObjectId.class)) {
      return (ParamConverter<T>) new ObjectIdConverter();
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

class ObjectIdConverter implements ParamConverter<ObjectId> {

  @Override
  public ObjectId fromString(final String hexString) {
    if (hexString == null || !ObjectId.isValid(hexString)) {
      throw new BadRequestException(String.format("invalid user id: '%s', should be 24 hexadecimal characters", hexString));
    }

    return new ObjectId(hexString);
  }

  @Override
  public String toString(final ObjectId id) {
    return id.toHexString();
  }

}
