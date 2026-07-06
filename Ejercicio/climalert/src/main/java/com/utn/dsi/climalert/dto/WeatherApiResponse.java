package com.utn.dsi.climalert.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherApiResponse(
        Location location,
        Current current
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Location(
            String name,
            String region,
            String country,
            String localtime
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Current(
            @JsonProperty("temp_c") double tempC,
            int humidity,
            Condition condition,
            @JsonProperty("wind_kph") double windKph,
            @JsonProperty("last_updated") String lastUpdated
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Condition(String text) {
    }
}
