package com.weather.forecast.controller;

import com.weather.forecast.data.Weather;
import com.weather.forecast.data.WeatherForecast;
import com.weather.forecast.data.WeatherTemperature;
import com.weather.forecast.model.Place;
import com.weather.forecast.service.ValidationService;
import com.weather.forecast.service.WeatherService;
import java.util.List;
import javax.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class MapController {

    private static final String ERROR = "error";
    private final WeatherService weatherService;

    private final String resourceString;

    private final ValidationService validationService;

    @GetMapping("/")
    public ModelAndView showForm() {
        return new ModelAndView("main", "place", new Place());
    }

    @GetMapping("/place")
    public String getWeather(@ModelAttribute("place") Place place,
            BindingResult result, ModelMap model) {

        log.info("Got weather request for place with lat={} and lon={}", place.getLat(), place.getLon());

        validationService.validate(place);

        if (result.hasErrors()) {
            return ERROR;
        }

        List<WeatherForecast> weatherForecastList = weatherService.getWeather(place.getLat(), place.getLon());

        model.addAttribute("dailyWeatherForecast", responseFromResource(weatherForecastList));

        return "forecast";
    }

    private String responseFromResource(List<WeatherForecast> weatherForecastList) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < weatherForecastList.size(); i++) {
            WeatherForecast weatherForecast = weatherForecastList.get(i);
            List<Weather> weathers = weatherForecast.getWeather();
            WeatherTemperature weatherTemperature = weatherForecast.getMain();

            res.append(resourceString.replace("%DATE%", String.valueOf(weatherForecast.getDt_txt()))
                    .replace("%MAIN%", weathers != null && !weathers.isEmpty() ? String.valueOf(weathers.get(0).getMain()) : Strings.EMPTY)
                    .replace("%ICON%", weathers != null && !weathers.isEmpty() ? String.valueOf(weathers.get(0).getIcon()) : Strings.EMPTY)
                    .replace("%TEMP%", String.valueOf(weatherTemperature.getTemp()))
                    .replace("%FEELS%", String.valueOf(weatherTemperature.getFeels_like()))
                    .replace("%MIN%", String.valueOf(weatherTemperature.getTemp_min()))
                    .replace("%MAX%", String.valueOf(weatherTemperature.getTemp_max())));
        }

        return res.toString();
    }

    @ExceptionHandler(ValidationException.class)
    public ModelAndView onConstraintValidationException(ValidationException e) {
        String message = e.getMessage();
        log.error("Exception occurred during a validation: {}", message);

        ModelAndView model = new ModelAndView(ERROR);
        model.addObject("exception", message);

        return model;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView onConstraintValidationException(Exception e) {
        String message = e.getMessage();
        log.error("Undefined exception occurred: {}", message);

        ModelAndView model = new ModelAndView(ERROR);
        model.addObject("exception", "Something went wrong");

        return model;
    }
}
