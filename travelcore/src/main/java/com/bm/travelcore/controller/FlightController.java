package com.bm.travelcore.controller;

import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.dto.BookFlightResDTO;
import com.bm.travelcore.dto.SearchFlightReqDTO;
import com.bm.travelcore.dto.SearchFlightResDTO;
import com.bm.travelcore.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping(value = "/search")
    public ResponseEntity<SearchFlightResDTO> searchFlight(@Valid @RequestBody SearchFlightReqDTO request) {
        return ResponseEntity.ok(flightService.searchFlights(request));
    }

    @PostMapping("/book")
    public ResponseEntity<BookFlightResDTO> bookFlight(@Valid @RequestBody BookFlightReqDTO request) {
        return ResponseEntity.ok(flightService.bookFlight(request));
    }
}
