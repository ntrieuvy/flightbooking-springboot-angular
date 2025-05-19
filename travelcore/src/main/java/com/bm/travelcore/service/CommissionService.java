package com.bm.travelcore.service;

import com.bm.travelcore.dto.CommissionDTO;
import com.bm.travelcore.model.Commission;
import com.bm.travelcore.model.Flight;
import com.bm.travelcore.model.OrderDetail;
import com.bm.travelcore.model.User;
import com.bm.travelcore.strategy.datacom.data.FlightSearchResData;

import java.util.List;

public interface CommissionService {

    FlightSearchResData applyCommission(FlightSearchResData datacomResDTO);

    void processCommissionFeeForOrderDetail(Long agencyId, OrderDetail orderDetail, Flight flight);

    List<Commission> findAllByUser(User user);

    List<Commission> addOrUpdateCommissions(User user, List<CommissionDTO> commissionListReqDTO);

    void deleteByUser(User user, Long id);
}
