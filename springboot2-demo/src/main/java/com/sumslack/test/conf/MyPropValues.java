package com.sumslack.test.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component  
@ConfigurationProperties(prefix="sumslack")
public class MyPropValues {
  public String listwhite;
  public String apidomain;
  public String getListwhite() {
      return listwhite;
  }
  public void setListwhite(String listwhite) {
      this.listwhite = listwhite;
  }
  public String getApidomain() {
      return apidomain;
  }
  public void setApidomain(String apidomain) {
      this.apidomain = apidomain;
  }
}