package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class Passport {
    @JsonProperty("DocumentType")
    private String documentType;

    @JsonProperty("DocumentCode")
    private String documentCode;

    @JsonProperty("DocumentExpiry")
    private String documentExpiry;

    @JsonProperty("Nationality")
    private String nationality;

    @JsonProperty("IssueCountry")
    private String issueCountry;
}
