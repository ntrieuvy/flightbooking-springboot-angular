package com.bm.travelcore.service.impl;

import com.bm.travelcore.dto.AirportReqDTO;
import com.bm.travelcore.dto.AirportResDTO;
import com.bm.travelcore.model.Airport;
import com.bm.travelcore.repository.AirportRepository;
import com.bm.travelcore.service.AirportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;

    @Override
    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    @Override
    public List<Airport> addAirports(List<AirportReqDTO> airportReqList) {
        for (AirportReqDTO airportReq : airportReqList) {
            Airport airport = Airport.
                    builder().
                    name(airportReq.getName()).
                    code(airportReq.getCode()).
                    build();

            airportRepository.save(airport);
        }
        return findAll();
    }

    @Override
    public void deleteById(Long id) {
        airportRepository.deleteById(id);
    }
}
