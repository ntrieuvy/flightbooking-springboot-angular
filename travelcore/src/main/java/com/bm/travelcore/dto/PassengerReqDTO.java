package com.bm.travelcore.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class PassengerReqDTO {
    private String firstName;
    private String lastName;
    private Integer parentId;
    private String type;
    private String dob;
    private String gender;
    private PassportReqDTO passport;
    private List<BaggageReqDTO> listBaggage;
}
