package com.weather.forecast.service;

import com.weather.forecast.model.Place;
import javax.validation.ValidationException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validate(Place pLace) {
        if (Strings.isBlank(pLace.getLat()) || Strings.isBlank(pLace.getLon())) {
            throw new ValidationException("Lat and lon values mustn't be empty");
        }
    }
}
