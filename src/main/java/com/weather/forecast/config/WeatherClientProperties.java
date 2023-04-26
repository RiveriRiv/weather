package com.weather.forecast.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "weather-client")
public class WeatherClientProperties {

    private String url;

    private String apiKey;
}
