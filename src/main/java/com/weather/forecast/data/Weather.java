package com.weather.forecast.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty(required = true)
    private String id;

    @JsonProperty(required = true)
    private String main;

    @JsonProperty(required = true)
    private String description;

    @JsonProperty(required = true)
    private String icon;

    @Override
    public String toString() {
        return "Main=" + main;
    }
}
