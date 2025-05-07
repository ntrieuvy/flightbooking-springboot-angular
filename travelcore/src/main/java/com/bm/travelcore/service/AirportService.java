package com.bm.travelcore.service;

import com.bm.travelcore.dto.AirportReqDTO;
import com.bm.travelcore.dto.AirportResDTO;
import com.bm.travelcore.model.Airport;

import java.util.List;

public interface AirportService {

    List<Airport> findAll();

    List<Airport> addAirports(List<AirportReqDTO> airportReqList);

    void deleteById(Long id);
}
