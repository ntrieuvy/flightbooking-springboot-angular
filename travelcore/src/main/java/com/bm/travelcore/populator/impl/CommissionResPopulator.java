package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.CommissionDTO;
import com.bm.travelcore.model.Commission;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class CommissionResPopulator implements Populator<Commission, CommissionDTO> {
    @Override
    public void populate(Commission source, CommissionDTO target) {
        target.setId(source.getId());
        target.setAgencyId(source.getAgency().getId());
        target.setAirline(source.getAirline().getCode());
        target.setAirportGroup(source.getAirportGroup().getName());
        target.setSelfServiceFeeAdt(source.getSelfServiceFeeAdt());
        target.setSelfServiceFeeInf(source.getSelfServiceFeeInf());
        target.setSelfServiceFeeChd(source.getSelfServiceFeeChd());
        target.setServiceFeeAdt(source.getServiceFeeAdt());
        target.setServiceFeeChd(source.getServiceFeeChd());
        target.setServiceFeeInf(source.getServiceFeeInf());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }
}
