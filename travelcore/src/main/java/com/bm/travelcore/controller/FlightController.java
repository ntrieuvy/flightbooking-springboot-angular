package com.bm.travelcore.controller;

import com.bm.travelcore.dto.*;
import com.bm.travelcore.service.FlightService;
import jakarta.mail.MessagingException;
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
    public ResponseEntity<BookFlightResDTO> bookFlight(@Valid @RequestBody BookFlightReqDTO request) throws MessagingException {
        return ResponseEntity.ok(flightService.bookFlight(request));
    }

    @PostMapping("/booking/hold")
    public ResponseEntity<BookFlightResDTO> holdFlight(@Valid @RequestBody BookFlightReqDTO request) {
        return ResponseEntity.ok(flightService.holdFlight(request));
    }

    @PostMapping("/cancel")
    public ResponseEntity<BookFlightResDTO> cancelFlights(@RequestParam("orderId") String orderId) {
        return ResponseEntity.ok(flightService.cancelFlights(orderId));
    }
}
