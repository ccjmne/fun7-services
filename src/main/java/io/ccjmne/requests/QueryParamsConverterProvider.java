package io.ccjmne.requests;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import com.neovisionaries.i18n.CountryCode;

@Provider
public class QueryParamsConverterProvider implements ParamConverterProvider {

  @Override
  @SuppressWarnings("unchecked")
  public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
    if (rawType == CountryCode.class) {
      return (ParamConverter<T>) new CountryCodeConverter();
    }

    return null;
  }

}

class CountryCodeConverter implements ParamConverter<CountryCode> {

  @Override
  public CountryCode fromString(final String cc) {
    final CountryCode res = CountryCode.getByCodeIgnoreCase(cc);
    if (null == res) {
      throw new BadRequestException(
        Response.status(BAD_REQUEST).entity(
          String.format(
            "Invalid country code: '%s'\nValid codes should follow the convention described by ISO 3166-1, alpha-2 or alpha-3.",
            cc
          )
        ).type(TEXT_PLAIN).build()
      );
    }

    return res;
  }

  @Override
  public String toString(final CountryCode value) {
    throw new UnsupportedOperationException();
  }

}
