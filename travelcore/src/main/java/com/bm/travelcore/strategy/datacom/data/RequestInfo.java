package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {
    @JsonProperty("PrivateKey")
    private String privateKey;
    @JsonProperty("ApiAccount")
    private String apiAccount;
    @JsonProperty("ApiPassword")
    private String apiPassword;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("Language")
    private String language;
    @JsonProperty("IpAddress")
    private String ipAddress;
}
