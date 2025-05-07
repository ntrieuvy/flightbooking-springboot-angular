package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Route {
    @JsonProperty("Leg")
    int leg;
    @JsonProperty("StartPoint")
    String startPoint;
    @JsonProperty("EndPoint")
    String endPoint;
    @JsonProperty("DepartDate")
    String departDate;
}
