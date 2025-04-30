package com.bm.travelcore.service;

import com.bm.travelcore.dto.SearchFlightReqDTO;
import com.bm.travelcore.dto.SearchFlightResDTO;
import jakarta.validation.Valid;

public interface FlightService {
    SearchFlightResDTO searchFlight(@Valid SearchFlightReqDTO request);
}
