package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.AgencyResDTO;
import com.bm.travelcore.model.Agency;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class AgencyListResPopulator implements Populator<Agency, AgencyResDTO>{
    @Override
    public void populate(Agency source, AgencyResDTO target) {
        target.setId(source.getId());
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setAddress(source.getAddress());
        target.setPhone(source.getPhone());
        target.setEmail(source.getEmail());
        target.setAddress(source.getAddress());
        target.setAccountBalance(source.getAccountBalance());
    }
}
