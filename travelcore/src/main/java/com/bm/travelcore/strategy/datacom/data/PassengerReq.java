package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PassengerReq {
    @JsonProperty("Index")
    private int index;

    @JsonProperty("ParentId")
    private Integer parentId;

    @JsonProperty("NameId")
    private String nameId;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Gender")
    private int gender;

    @JsonProperty("GivenName")
    private String givenName;

    @JsonProperty("Surname")
    private String surname;

    @JsonProperty("DateOfBirth")
    private String dateOfBirth;

    @JsonProperty("PassengerId")
    private String passengerId;

    @JsonProperty("Passport")
    private Passport passport;

    @JsonProperty("ListBaggage")
    private List<BaggageReq> listBaggage;

    @JsonProperty("ListPreSeat")
    private List<PreSeatReq> listPreSeat;

    @JsonProperty("ListService")
    private List<Service> listService;

    @JsonProperty("ListFareInfo")
    private List<FareItem> listFareInfo;

    @JsonProperty("ListMembership")
    private List<Membership> listMembership;
}

