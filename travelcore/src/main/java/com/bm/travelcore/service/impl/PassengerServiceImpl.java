package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.Order;
import com.bm.travelcore.model.Passenger;
import com.bm.travelcore.repository.PassengerRepository;
import com.bm.travelcore.service.PassengerService;
import com.bm.travelcore.strategy.datacom.data.PassengerRes;
import com.bm.travelcore.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final Helper helper;

    @Override
    public Passenger storePassengerForBooking(Order order, PassengerRes passenger) {
        return passengerRepository.save(
                Passenger.builder()
                        .order(order)
                        .firstName(passenger.getGivenName())
                        .lastName(passenger.getSurname())
                        .passenger_index(passenger.getIndex())
                        .type(passenger.getType())
                        .birthday(LocalDate.parse(passenger.getDateOfBirth()))
                        .gender(helper.ConvertGenderToString(passenger.getGender()))
                        .build()
        );
    }
}
