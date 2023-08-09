package com.api.response;

import com.api.response.items.MotorGetItem;
import com.api.response.items.MotorSetPinsItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/*
{
        "motor":        {
                "steppers":     [{
                                "stepperid":    0,
                                "position":     0
                        }, {
                                "stepperid":    1,
                                "position":     0
                        }, {
                                "stepperid":    2,
                                "position":     0
                        }, {
                                "stepperid":    3,
                                "position":     0
                        }]
        }
}
 */
@JsonTypeName("motor")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class MotorGetResponse {

    @JsonProperty("steppers")
    public MotorGetItem motorGetItem[];
}
