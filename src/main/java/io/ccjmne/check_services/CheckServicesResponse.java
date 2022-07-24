package io.ccjmne.check_services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.KebabCaseStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonNaming(KebabCaseStrategy.class)
public class CheckServicesResponse {

  @JsonSerialize(using = BooleanSerializer.class)
  Boolean multiplayer;
  @JsonSerialize(using = BooleanSerializer.class)
  Boolean userSupport;
  @JsonSerialize(using = BooleanSerializer.class)
  Boolean ads;

  public CheckServicesResponse(final Boolean multiplayer, final Boolean userSupport, final Boolean ads) {
    this.multiplayer = multiplayer;
    this.userSupport = userSupport;
    this.ads = ads;
  }

}

class BooleanSerializer extends JsonSerializer<Boolean> {

  @Override
  public void serialize(final Boolean bool, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    gen.writeString(bool ? "enabled" : "disabled");
  }

}
