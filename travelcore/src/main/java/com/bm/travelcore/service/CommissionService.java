package com.bm.travelcore.service;

import com.bm.travelcore.strategy.datacom.data.FlightSearchResData;

public interface CommissionService {

    FlightSearchResData applyCommission(FlightSearchResData datacomResDTO);
}
