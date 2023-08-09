package com.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MacRequest {
    public String mac;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer psx;
}
