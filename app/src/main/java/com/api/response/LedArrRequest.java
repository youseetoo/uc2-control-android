package com.api.response;

import com.api.response.items.LedColorItem;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("led")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class LedArrRequest
{
    public int LEDArrMode =0;
    public LedColorItem led_array[];
}
