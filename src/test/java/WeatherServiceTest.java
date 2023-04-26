import com.weather.forecast.client.OpenWeatherClient;
import com.weather.forecast.data.DailyWeatherForecast;
import com.weather.forecast.data.WeatherForecast;
import com.weather.forecast.data.WeatherTemperature;
import com.weather.forecast.service.WeatherService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    private WeatherService weatherService;

    @Mock
    private OpenWeatherClient openWeatherClient;

    @BeforeEach
    public void before() {
        weatherService = new WeatherService(openWeatherClient);
    }

    @Test
    void shouldReturnForecastIfWeatherForecastListIsNotNull() {
        DailyWeatherForecast dailyWeatherForecast = createDailyWeatherForecast();
        when(openWeatherClient.getWeather(anyString(), anyString())).thenReturn(Optional.of(dailyWeatherForecast));

        List<WeatherForecast> result = weatherService.getWeather("5", "5");

        assertThat(dailyWeatherForecast.getList().stream().toList()).isEqualTo(result);
    }

    @Test
    void shouldReturnEmptyForecastIfWeatherForecastListIsNull() {
        DailyWeatherForecast dailyWeatherForecast = new DailyWeatherForecast();

        when(openWeatherClient.getWeather(anyString(), anyString())).thenReturn(Optional.of(dailyWeatherForecast));

        List<WeatherForecast> result = weatherService.getWeather("5", "5");

        assertThat(0).isEqualTo(result.size());
    }

    @Test
    void shouldReturnEmptyForecastIfWeatherResponseIsEmpty() {
        when(openWeatherClient.getWeather(anyString(), anyString())).thenReturn(Optional.empty());

        List<WeatherForecast> result = weatherService.getWeather("5", "5");

        assertThat(0).isEqualTo(result.size());
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
