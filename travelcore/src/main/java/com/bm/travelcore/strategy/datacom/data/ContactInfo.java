package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactInfo {
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Area")
    private String area;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Remark")
    private String remark;

    @JsonProperty("ReceiveEmail")
    private boolean receiveEmail;
}
