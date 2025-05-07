package com.bm.travelcore.controller;

import com.bm.travelcore.dto.AirportReqDTO;
import com.bm.travelcore.dto.AirportResDTO;
import com.bm.travelcore.model.Airport;
import com.bm.travelcore.populator.Populator;
import com.bm.travelcore.populator.impl.AirportResPopulator;
import com.bm.travelcore.service.AirportService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/airports")
@AllArgsConstructor
public class AirportController {

    private final AirportService airportService;
    private AirportResPopulator responsePopular;

    @GetMapping
    public ResponseEntity<List<AirportResDTO>> getAirports() {
        List<Airport> airports = airportService.findAll();
        List<AirportResDTO> response = new ArrayList<>();
        responsePopular.populate(airports, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<List<AirportResDTO>> addAirports(@RequestBody List<AirportReqDTO> airportReqList) {
        List<Airport> airports = airportService.addAirports(airportReqList);
        List<AirportResDTO> response = new ArrayList<>();
        responsePopular.populate(airports, response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
        airportService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
