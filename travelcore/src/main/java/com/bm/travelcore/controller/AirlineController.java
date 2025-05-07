package com.bm.travelcore.controller;

import com.bm.travelcore.dto.AgencyResDTO;
import com.bm.travelcore.dto.AirlineResDTO;
import com.bm.travelcore.model.Airline;
import com.bm.travelcore.populator.Populator;
import com.bm.travelcore.populator.impl.AirlineResPopulator;
import com.bm.travelcore.service.AirlineService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/airlines")
public class AirlineController {

    private final AirlineService airlineService;
    private final AirlineResPopulator airlineResDTOPopulator;

    @GetMapping
    public ResponseEntity<List<AirlineResDTO>> getAllAirlines() {
        return ResponseEntity.ok(airlineService.findAll().stream()
                .map(airline -> {
                    AirlineResDTO airlineResDTO = new AirlineResDTO();
                    airlineResDTOPopulator.populate(airline, airlineResDTO);
                    return airlineResDTO;
                })
                .collect(Collectors.toList()));
    }
}
