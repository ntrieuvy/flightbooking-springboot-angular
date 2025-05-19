package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.Baggage;
import com.bm.travelcore.model.Passenger;
import com.bm.travelcore.repository.BaggageRepository;
import com.bm.travelcore.service.BaggageService;
import com.bm.travelcore.service.UserService;
import com.bm.travelcore.strategy.datacom.data.BaggageRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BaggageServiceImpl implements BaggageService {
    private final BaggageRepository baggageRepository;
    private final UserService userService;

    @Override
    public void storeBaggageForBooking(Passenger passengerForBooking, BaggageRes baggage) {
        baggageRepository.save(
                Baggage.builder()
                        .leg(baggage.getLeg())
                        .name(baggage.getName())
                        .airline(baggage.getAirline())
                        .price(baggage.getPrice())
                        .value(baggage.getValue())
                        .statusCode(baggage.getStatusCode())
                        .currency(baggage.getCurrency())
                        .passenger(passengerForBooking)
                        .session(baggage.getSession())
                        .confirmed(baggage.isConfirmed())
                        .description(baggage.getDescription())
                        .type(baggage.getType())
                        .paxType(baggage.getPaxType())
                        .system(baggage.getSystem())
                        .startPoint(baggage.getStartPoint())
                        .endPoint(baggage.getEndPoint())
                        .build()
        );
    }
}
