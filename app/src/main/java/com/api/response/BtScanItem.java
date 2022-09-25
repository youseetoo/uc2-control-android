package com.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BtScanItem {
    @JsonProperty("name")
    public String name;
    @JsonProperty("mac")
    public String mac;
}
