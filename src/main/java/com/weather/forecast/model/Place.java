package com.weather.forecast.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Place {

    @NotNull
    @NotBlank
    private String lat;

    @NotNull
    @NotBlank
    private String lon;
}
