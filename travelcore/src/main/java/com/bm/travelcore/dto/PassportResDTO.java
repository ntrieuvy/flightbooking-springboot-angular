package com.bm.travelcore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PassportResDTO {
    private String documentType;
    private String documentCode;
    private String documentExpiry;
    private String nationality;
    private String issueCountry;
}
