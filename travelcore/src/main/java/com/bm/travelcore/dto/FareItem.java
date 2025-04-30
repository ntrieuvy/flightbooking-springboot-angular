package com.bm.travelcore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FareItem {
    @JsonProperty("Code")
    private String code;

    @JsonProperty("Amount")
    private double amount;

    @JsonProperty("Name")
    private String name;
}
