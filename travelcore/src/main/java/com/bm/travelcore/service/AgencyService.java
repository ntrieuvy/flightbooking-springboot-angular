package com.bm.travelcore.service;

import com.bm.travelcore.dto.AgencyReqDTO;
import com.bm.travelcore.model.Agency;

import java.util.List;

public interface AgencyService {
    List<Agency> getAll(int page, int pageSize);
    Agency create(AgencyReqDTO request);
    Agency getByCode(String code);
    void delete(Long id);
    void update(Long id, AgencyReqDTO request);
}
