package com.bm.travelcore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AirOptionReqDTO {
    private String session;
    private String sessionType;
    private int airlineOptionId;
    private int fareOptionId;
    private int flightOptionId;
}
