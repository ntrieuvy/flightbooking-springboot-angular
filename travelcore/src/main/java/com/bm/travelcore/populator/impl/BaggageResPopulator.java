package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.BaggageResDTO;
import com.bm.travelcore.model.Baggage;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class BaggageResPopulator implements Populator<Baggage, BaggageResDTO> {

    @Override
    public void populate(Baggage source, BaggageResDTO target) {
        target.setAirline(source.getAirline());
        target.setCode(source.getSystem());
        target.setCurrency(source.getCurrency());
        target.setName(source.getName());
        target.setRoute(source.getStartPoint() + " - " + source.getEndPoint());
        target.setValue(source.getValue());
        target.setLeg(source.getLeg());
        target.setPrice(source.getPrice());
    }
}