package com.api.response.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MotorActItem
{
    public int stepperid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long speed = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long maxspeed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long acceleration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long position;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int isforever;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int isaccel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int isabs;
}
