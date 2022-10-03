package com.api.response;

import com.api.response.items.MotorGetItem;
import com.api.response.items.MotorSetPinsItem;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MotorGetResponse {

    @JsonProperty("steppers")
    public MotorGetItem motorGetItem[];
}
