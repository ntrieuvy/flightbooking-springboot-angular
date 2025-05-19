package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.BaggageResDTO;
import com.bm.travelcore.dto.PassengerResDTO;
import com.bm.travelcore.dto.PassportResDTO;
import com.bm.travelcore.model.Passenger;
import com.bm.travelcore.model.Passport;
import com.bm.travelcore.populator.Populator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerResPopulator implements Populator<Passenger, PassengerResDTO> {

    private final PassportResPopulator passportPopulator;
    private final BaggageResPopulator baggagePopulator;

    @Override
    public void populate(Passenger source, PassengerResDTO target) {
        target.setType(source.getType());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setGender(source.getGender());
        target.setDob(source.getBirthday());

        if (source.getPassport() != null) {
            PassportResDTO passportDTO = new PassportResDTO();
            passportPopulator.populate(source.getPassport(), passportDTO);
            target.setPassport(passportDTO);
        }

        if (source.getPassengerBaggageList() != null && !source.getPassengerBaggageList().isEmpty()) {
            target.setBaggages(source.getPassengerBaggageList().stream()
                    .map(baggage -> {
                        BaggageResDTO dto = new BaggageResDTO();
                        baggagePopulator.populate(baggage, dto);
                        return dto;
                    })
                    .toList());
        }
    }
}