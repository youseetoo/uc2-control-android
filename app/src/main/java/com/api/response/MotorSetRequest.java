package com.api.response;

import com.api.response.items.MotorSetPinsItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("motor")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class MotorSetRequest {

    @JsonProperty("steppers")
    public MotorSetPinsItem motorSetPinsItem[];
}
