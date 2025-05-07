package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.AirportResDTO;
import com.bm.travelcore.model.Airport;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AirportResPopulator implements Populator<List<Airport>, List<AirportResDTO>> {

    @Override
    public void populate(List<Airport> source, List<AirportResDTO> target) {
        for (Airport airport : source) {
            AirportResDTO airportResDTO = AirportResDTO
                    .builder()
                    .id(airport.getId())
                    .code(airport.getCode())
                    .name(airport.getName())
                    .build();
            target.add(airportResDTO);
        }
    }
}
