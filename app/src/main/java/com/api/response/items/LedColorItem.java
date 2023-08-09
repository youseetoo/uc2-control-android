package com.api.response.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LedColorItem {

    public int id;
    public int r;
    public int g;
    public int b;
}
