package com.bm.travelcore.controller;

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

    @PostMapping
    public ResponseEntity<SearchFlightResDTO> searchFlight(@Valid @RequestBody SearchFlightReqDTO request) {
        return ResponseEntity.ok(flightService.searchFlight(request));
    }
}
