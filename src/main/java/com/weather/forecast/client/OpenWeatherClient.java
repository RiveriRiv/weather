package com.weather.forecast.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.weather.forecast.config.WeatherClientProperties;
import com.weather.forecast.data.DailyWeatherForecast;

@AllArgsConstructor
@Service
@Slf4j
public class OpenWeatherClient {

    private RestTemplate weatherRestTemplate;

    private WeatherClientProperties weatherClientProperties;

    @Cacheable("forecast")
    public Optional<DailyWeatherForecast> getWeather(String lat, String lon) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("lat", lat);
        uriVariables.put("lon", lon);
        uriVariables.put("apiKey", weatherClientProperties.getApiKey());

        log.info("Start requesting weather for lat={}, lon={}", lat, lon);

        try {
            DailyWeatherForecast response = weatherRestTemplate.getForObject(weatherClientProperties.getUrl(), DailyWeatherForecast.class, uriVariables);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Something went wrong during weather request: {}", e.getMessage());
        }

        return Optional.empty();
    }
}
