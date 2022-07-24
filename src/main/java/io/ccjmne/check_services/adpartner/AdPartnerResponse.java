package io.ccjmne.check_services.adpartner;

public class AdPartnerResponse {

  public static final String YES = "sure, why not!";
  public static final String NO  = "you shall not pass!";

  public String ads;

  public AdPartnerResponse() {}

  public AdPartnerResponse(final String ads) {
    this.ads = ads;
  }

  public boolean isYes() {
    return YES.equals(this.ads);
  }

}
