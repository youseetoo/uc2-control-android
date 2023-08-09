package com.api.response.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MotorGetItem {
    public int stepperid;
    public int position;
    public boolean isActivated;
}
