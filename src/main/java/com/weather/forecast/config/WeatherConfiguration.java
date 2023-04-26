package com.weather.forecast.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.FileCopyUtils;

@Configuration
@EnableCaching
public class WeatherConfiguration {

    @Value("classpath:resource.txt")
    private Resource resource;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("forecast");
    }

    @CacheEvict(value = "forecast", allEntries = true)
    @Scheduled(fixedRateString = "${caching.spring.forecastListTTL}")
    public void emptyForecastCache() {
    }

    @Bean
    public String resourceString() {
        return readFileToString(resource);
    }

    public String readFileToString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
