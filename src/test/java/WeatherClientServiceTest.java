import com.weather.forecast.client.OpenWeatherClient;
import com.weather.forecast.config.WeatherClientProperties;
import com.weather.forecast.data.DailyWeatherForecast;
import com.weather.forecast.data.WeatherForecast;
import com.weather.forecast.data.WeatherTemperature;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WeatherClientServiceTest {

    @Mock
    private RestTemplate weatherRestTemplate;

    @Mock
    private WeatherClientProperties weatherClientProperties;

    private OpenWeatherClient openWeatherClient;

    @BeforeEach
    public void before() {
        openWeatherClient = new OpenWeatherClient(weatherRestTemplate, weatherClientProperties);
    }

    @Test
    void shouldReturnEmptyResponseIfGotException() {
        when(weatherClientProperties.getApiKey()).thenReturn("ApiKey");
        when(weatherClientProperties.getUrl()).thenReturn("http://localhost:8080");
        when(weatherRestTemplate.getForObject(anyString(), any(), anyMap())).thenThrow(new RuntimeException());

        Optional<DailyWeatherForecast> result = openWeatherClient.getWeather("5", "5");

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void shouldReturnForecastIfGotSuccessResponse() {
        DailyWeatherForecast dailyWeatherForecast = createDailyWeatherForecast();

        when(weatherClientProperties.getApiKey()).thenReturn("ApiKey");
        when(weatherClientProperties.getUrl()).thenReturn("http://localhost");
        when(weatherRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(dailyWeatherForecast);

        Optional<DailyWeatherForecast> result = openWeatherClient.getWeather("5", "5");

        assertThat(result).isEqualTo(Optional.of(dailyWeatherForecast));
    }

    private DailyWeatherForecast createDailyWeatherForecast() {
        WeatherForecast weatherForecast = WeatherForecast.builder()
                .weather(new ArrayList<>())
                .main(new WeatherTemperature())
                .build();

        TreeSet<WeatherForecast> treeSet = new TreeSet<>();
        treeSet.add(weatherForecast);
        return DailyWeatherForecast.builder()
                .list(treeSet)
                .build();
    }
}
