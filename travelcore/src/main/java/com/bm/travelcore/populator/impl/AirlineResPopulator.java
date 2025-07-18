package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.AirlineResDTO;
import com.bm.travelcore.model.Airline;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class AirlineResPopulator implements Populator<Airline, AirlineResDTO> {
    @Override
    public void populate(Airline source, AirlineResDTO target) {
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setLogo(source.getLogo());
        target.setId(source.getId());
        target.setIsActive(source.getIsActive());
    }
}
