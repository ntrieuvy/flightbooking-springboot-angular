package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.FlightResDTO;
import com.bm.travelcore.model.Flight;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class FlightResPopulator implements Populator<Flight, FlightResDTO> {

    @Override
    public void populate(Flight source, FlightResDTO target) {
        target.setId(source.getId());
        target.setUniqueKey(source.getUniqueKey());
        target.setCode(source.getCode());
        target.setAirline(source.getAirline());
        target.setDeparture(source.getDeparture());
        target.setArrival(source.getArrival());
        target.setDate(source.getDate().toString());
        target.setStd(source.getStd().toString());
        target.setSta(source.getSta().toString());
        target.setNumber(source.getNumber());
        target.setDuration(source.getDuration());
        target.setGroupClass(source.getGroupClass());
        target.setFareClass(source.getFareClass());
        target.setPromo(source.getPromo());
        target.setSegments(source.getSegments());
        target.setHandBaggage(source.getHandBaggage());
        target.setAllowanceBaggage(source.getAllowanceBaggage());
        target.setOperating(source.getOperating());
    }
}