package com.weather.forecast.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class WeatherForecast implements Comparable<WeatherForecast> {

    @JsonProperty(required = true)
    private List<Weather> weather;

    @JsonProperty(required = true)
    private WeatherTemperature main;

    private String dt_txt;

    @JsonProperty(required = true)
    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt.split(" ")[0];
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (dt_txt != null) {
            result = 31 * result + dt_txt.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof WeatherForecast other))
            return false;

        return (this.dt_txt == null && other.dt_txt == null)
                || (this.dt_txt != null && this.dt_txt.equals(other.dt_txt));
    }

    @Override
    public int compareTo(WeatherForecast o) {
        if (this.dt_txt == null || o.dt_txt == null) {
            return 0;
        }

        return this.dt_txt.compareTo(o.dt_txt);
    }

    @Override
    public String toString() {
        return "\n\nDate:" + dt_txt + ", \nWeather:" + weather +
                ", \nTemperature:" + main;
    }
}
