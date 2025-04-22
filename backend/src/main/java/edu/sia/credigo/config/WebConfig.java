package edu.sia.credigo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOriginPatterns("http://locahost:5173")
        .allowedMethods("GET", "POST", "DELETE", "OPTIONS").allowedHeaders("*").allowCredentials(true);
  }
}
