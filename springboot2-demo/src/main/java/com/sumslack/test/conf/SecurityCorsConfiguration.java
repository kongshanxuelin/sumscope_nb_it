package com.sumslack.test.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityCorsConfiguration {
  @Autowired
  private MyPropValues myPropValues;
  @Bean
  public FilterRegistrationBean corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      //来自API网站的所有接口允许跨域访问
      config.addAllowedOrigin(myPropValues.getApidomain());
      config.addAllowedHeader(CorsConfiguration.ALL);
      config.addAllowedMethod(CorsConfiguration.ALL);
      source.registerCorsConfiguration("/**", config);
      FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
      bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
      return bean;
  }
}