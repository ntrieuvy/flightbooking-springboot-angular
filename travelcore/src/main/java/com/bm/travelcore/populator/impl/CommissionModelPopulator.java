package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.CommissionDTO;
import com.bm.travelcore.model.Airline;
import com.bm.travelcore.model.AirportGroup;
import com.bm.travelcore.model.Commission;
import com.bm.travelcore.model.User;
import com.bm.travelcore.populator.Populator;
import com.bm.travelcore.repository.AirlineRepository;
import com.bm.travelcore.repository.AirportGroupRepository;
import com.bm.travelcore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommissionModelPopulator implements Populator<CommissionDTO, Commission> {
    private final AirlineRepository airlineRepository;
    private final AirportGroupRepository airportGroupRepository;
    private final UserService userService;

    @Override
    public void populate(CommissionDTO source, Commission target) {

        Airline airline = airlineRepository.findByCode(source.getAirline());
        if (airline != null) {
            target.setAirline(airline);
        }

        AirportGroup airportGroup = airportGroupRepository.findByName(source.getAirportGroup());
        if (airportGroup != null) {
            target.setAirportGroup(airportGroup);
        }

        target.setSelfServiceFeeAdt(source.getSelfServiceFeeAdt());
        target.setSelfServiceFeeInf(source.getSelfServiceFeeInf());
        target.setSelfServiceFeeChd(source.getSelfServiceFeeChd());
        target.setServiceFeeAdt(source.getServiceFeeAdt());
        target.setServiceFeeChd(source.getServiceFeeChd());
        target.setServiceFeeInf(source.getServiceFeeInf());

        if (source.getId() == null) {
            User user = userService.getCurrentAccount();
            target.setUser(user);
            target.setAgency(user.getAgency());
        }
    }
}
