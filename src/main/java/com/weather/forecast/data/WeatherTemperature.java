package com.weather.forecast.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
@Getter
public class WeatherTemperature {

    @JsonProperty(required = true)
    private int temp;

    @JsonProperty(required = true)
    private int feels_like;

    @JsonProperty(required = true)
    private int temp_min;

    @JsonProperty(required = true)
    private int temp_max;

    @Override
    public String toString() {
        return "\nTemp=" + this.temp + ", feels like=" + this.feels_like + ", min=" + this.temp_min + ", max=" + this.temp_max;
    }
}
