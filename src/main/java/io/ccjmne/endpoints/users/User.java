package io.ccjmne.endpoints.users;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import com.neovisionaries.i18n.CountryCode;

public class User {

  @BsonId()
  public ObjectId    id;
  public CountryCode country;
  public Integer     apiCalls = 0;

  public User() {}

  public User(final ObjectId id) {
    this.id = id;
  }

  public User(final ObjectId id, final CountryCode country, final Integer apiCalls) {
    this(id);
    this.country = country;
    this.apiCalls = apiCalls;
  }

  public User checkedInFrom(final CountryCode country) {
    return new User(this.id, country, this.apiCalls + 1);
  }

}
