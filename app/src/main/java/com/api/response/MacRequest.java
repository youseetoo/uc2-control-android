package com.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MacRequest {
    public String mac;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer psx;
}
