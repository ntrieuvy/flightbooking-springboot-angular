package com.bm.travelcore.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResDTO {
    private String type;
    private String firstName;
    private String lastName;
    private Integer parentId;
    private LocalDate dob;
    private String gender;
    private PassportResDTO passport;
    private List<BaggageResDTO> baggages;
}
