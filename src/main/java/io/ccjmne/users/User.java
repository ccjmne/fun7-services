package io.ccjmne.users;

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

  public User checkedInFrom(final CountryCode country) {
    this.apiCalls++;
    this.country = country;
    return this;
  }

}
