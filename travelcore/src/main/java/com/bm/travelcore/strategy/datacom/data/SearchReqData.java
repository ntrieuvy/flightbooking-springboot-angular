package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchReqData {
    @JsonProperty("RequestInfo")
    RequestInfo requestInfo;
    @JsonProperty("System")
    String system;
    @JsonProperty("Adt")
    int adt;
    @JsonProperty("Chd")
    int chd;
    @JsonProperty("Inf")
    int inf;
    @JsonProperty("Tourcode")
    String tourcode;
    @JsonProperty("ListRoute")
    List<Route> listRoute;
}
