import com.weather.forecast.Application;
import com.weather.forecast.data.DailyWeatherForecast;
import com.weather.forecast.data.Weather;
import com.weather.forecast.data.WeatherForecast;
import com.weather.forecast.data.WeatherTemperature;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
class AppTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private String resourceString;

    @MockBean
    private RestTemplate weatherRestTemplate;

    @Test
    void shouldReturnSuccessResponse() throws Exception {
        DailyWeatherForecast dailyWeatherForecast = createDailyWeatherForecast();
        when(weatherRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(dailyWeatherForecast);

        mvc.perform(get("/place?lat=5&lon=5").headers(new HttpHeaders()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("dailyWeatherForecast", responseFromResource(dailyWeatherForecast.getList().stream().toList())));
    }

    @Test
    void shouldReturnErrorIfLatOrLngAreNull() throws Exception {
        DailyWeatherForecast dailyWeatherForecast = createDailyWeatherForecast();
        when(weatherRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(dailyWeatherForecast);

        mvc.perform(get("/place").headers(new HttpHeaders()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("exception", "Lat and lon values mustn't be empty"));
    }

    private String responseFromResource(List<WeatherForecast> weatherForecastList) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < weatherForecastList.size(); i++) {
            WeatherForecast weatherForecast = weatherForecastList.get(i);
            List<Weather> weathers = weatherForecast.getWeather();
            WeatherTemperature weatherTemperature = weatherForecast.getMain();

            res.append(resourceString.replace("%DATE%", String.valueOf(weatherForecast.getDt_txt()))
                    .replace("%MAIN%", weathers != null && weathers.size() > 0 ? String.valueOf(weathers.get(0).getMain()) : Strings.EMPTY)
                    .replace("%ICON%", weathers != null && weathers.size() > 0 ? String.valueOf(weathers.get(0).getIcon()) : Strings.EMPTY)
                    .replace("%TEMP%", String.valueOf(weatherTemperature.getTemp()))
                    .replace("%FEELS%", String.valueOf(weatherTemperature.getFeels_like()))
                    .replace("%MIN%", String.valueOf(weatherTemperature.getTemp_min()))
                    .replace("%MAX%", String.valueOf(weatherTemperature.getTemp_max())));
        }

        return res.toString();
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
