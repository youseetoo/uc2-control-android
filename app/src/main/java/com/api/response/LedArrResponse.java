package com.api.response;

import com.api.enums.LedModes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LedArrResponse
{
    @JsonProperty("ledArrNum")
    public int ledArrNum;

    @JsonProperty("led_ison")
    public boolean is_on;

    @JsonProperty("ledArrPin")
    public int pin;

    @JsonProperty("LEDArrMode")
    public LedModes[] ledModes;
}
