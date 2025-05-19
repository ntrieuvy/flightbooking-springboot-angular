package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.PassportResDTO;
import com.bm.travelcore.model.Passport;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class PassportResPopulator implements Populator<Passport, PassportResDTO> {

    @Override
    public void populate(Passport source, PassportResDTO target) {
        target.setDocumentType(source.getDocumentType());
        target.setDocumentCode(source.getDocumentCode());
        target.setDocumentExpiry(source.getDocumentExpiry());
        target.setNationality(source.getNationality());
        target.setIssueCountry(source.getIssueCountry());
    }
}