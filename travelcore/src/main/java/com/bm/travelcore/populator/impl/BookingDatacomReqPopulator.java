package com.bm.travelcore.populator.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.dto.BaggageReqDTO;
import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.dto.ContactReqDTO;
import com.bm.travelcore.dto.PassengerReqDTO;
import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.Title;
import com.bm.travelcore.populator.Populator;
import com.bm.travelcore.service.UserService;
import com.bm.travelcore.strategy.datacom.data.*;
import com.bm.travelcore.utils.Helper;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingDatacomReqPopulator implements Populator<BookFlightReqDTO, BookingReqData> {
    private final String AREA = "+84";
    private final String KEY = "BTS";
    private final String KEY_SEPARATOR = "-";
    private final String SPACE_SEPARATOR = " ";
    private final UserService userService;
    private final Helper helper;

    @Override
    public void populate(BookFlightReqDTO source, BookingReqData target) {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8);
        String requestKey = KEY + timePart + KEY_SEPARATOR + randomPart;


        target.setSystem(StringUtil.EMPTY_STRING);
        target.setRequestKey(requestKey);
        target.setGuestContact(setContactInfo(source.getContact(), Boolean.FALSE));

        User user = userService.getSysAccount();
        target.setAgentContact(
                setContactInfo(
                        ContactReqDTO.builder()
                                .gender("MALE")
                                .fullName(user.getAgency().getName())
                                .phone(user.getAgency().getPhone())
                                .email(user.getAgency().getEmail())
                                .build()
                        , Boolean.TRUE));

        List<PassengerReq> passengerReqs = new ArrayList<>();
        for (PassengerReqDTO passenger : source.getListPassenger()) {
            passengerReqs.add(
                    PassengerReq.builder()
                            .index(source.getListPassenger().indexOf(passenger) + 1)
                            .parentId(passenger.getParentId())
                            .title(helper.getTitleByGender(passenger.getGender()))
                            .type(passenger.getType())
                            .gender(passenger.getGender().equals("MALE") ? 0 : 1)
                            .givenName(passenger.getFirstName())
                            .surname(passenger.getLastName())
                            .dateOfBirth(passenger.getDob())
                            .passport(
                                    Passport.builder()
                                            .documentCode(passenger.getPassport().getDocumentCode())
                                            .documentType(passenger.getPassport().getDocumentType())
                                            .documentExpiry(passenger.getPassport().getDocumentExpiry())
                                            .nationality(passenger.getPassport().getNationality())
                                            .issueCountry(passenger.getPassport().getIssueCountry())
                                            .build()
                            )
                            .listBaggage(setListBaggage(passenger.getListBaggage()))
                            .build()
            );
        }

        target.setListPassenger(passengerReqs);

        List<AirOptionReq> airOptionReqs = new ArrayList<>();
        source.getListAirOption().forEach(fare -> {
            airOptionReqs.add(
                    AirOptionReq.builder()
                            .session(fare.getSession())
                            .sessionType(fare.getSessionType())
                            .airlineOptionId(fare.getAirlineOptionId())
                            .fareOptionId(fare.getFareOptionId())
                            .flightOptionId(fare.getFlightOptionId())
                            .build()
            );
        });
        target.setListAirOptionRes(airOptionReqs);

        target.setOption(
                OptionBookingReq.builder()
                        .separateBooking(Boolean.FALSE)
                        .issueTicket(Boolean.FALSE)
                        .sendEmail(Boolean.TRUE)
                        .build()
        );
    }

    private List<BaggageReq> setListBaggage(List<BaggageReqDTO> listBaggage) {
        if (listBaggage == null) {
            return null;
        }
        List<BaggageReq> baggageReqs = new ArrayList<>();
        listBaggage.forEach(baggage -> {
            baggageReqs.add(
                    BaggageReq.builder()
                            .value(baggage.getValue())
                            .session(UUID.randomUUID().toString())
                            .build()
            );
        });
        return baggageReqs;
    }

    private ContactInfo setContactInfo(ContactReqDTO source, Boolean receiveEmail) {
        return ContactInfo.builder()
                .title(helper.getTitleByGender(source.getGender()))
                .name(source.getFullName())
                .area(AREA)
                .email(source.getEmail())
                .phone(source.getPhone())
                .address(StringUtil.EMPTY_STRING)
                .remark(StringUtil.EMPTY_STRING)
                .receiveEmail(receiveEmail)
                .build();
    }
}
