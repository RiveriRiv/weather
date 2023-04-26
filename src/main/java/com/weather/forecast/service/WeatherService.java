package com.weather.forecast.service;

import com.weather.forecast.client.OpenWeatherClient;
import com.weather.forecast.data.DailyWeatherForecast;
import com.weather.forecast.data.WeatherForecast;
import java.util.List;
import java.util.TreeSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;

    public List<WeatherForecast> getWeather(String lat, String lon) {
        List<WeatherForecast> weatherForecasts = openWeatherClient.getWeather(lat, lon)
                .map(DailyWeatherForecast::getList)
                .orElse(new TreeSet<>())
                .stream().toList();

        log.info("\nSuccessfully got weather forecast for lat={} and lon={}: {}", lat, lon, weatherForecasts);

        return weatherForecasts;
    }
}
