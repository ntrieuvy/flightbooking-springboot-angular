package com.bm.travelcore.service;

import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.dto.BookFlightResDTO;
import com.bm.travelcore.dto.SearchFlightReqDTO;
import com.bm.travelcore.dto.SearchFlightResDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface FlightService {
    SearchFlightResDTO searchFlights(@Valid SearchFlightReqDTO request);

    BookFlightResDTO bookFlight(@Valid BookFlightReqDTO request) throws MessagingException;

    BookFlightResDTO holdFlight(@Valid BookFlightReqDTO request);

    BookFlightResDTO cancelFlights(String orderId);
}
