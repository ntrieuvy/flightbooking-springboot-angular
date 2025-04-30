package com.bm.travelcore.service.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.dto.*;
import com.bm.travelcore.factory.FlightProviderFactory;
import com.bm.travelcore.service.CommissionService;
import com.bm.travelcore.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import io.micrometer.core.instrument.MeterRegistry;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final ApplicationProperties applicationProperties;
    private final FlightProviderFactory flightProviderFactory;
    private final CommissionService commissionService;

    private final MeterRegistry meterRegistry;

    @Override
    public SearchFlightResDTO searchFlight(SearchFlightReqDTO request) {

        SearchFlightResDTO response = new SearchFlightResDTO();

        Object result = flightProviderFactory.getStrategy(applicationProperties.getProviderFlightApiDefault())
                .searchFlights(request);

        if (result instanceof FlightSearchDatacomResDTO responseDatacom) {
            long startTime = System.currentTimeMillis();
            try {
                response.setData(commissionService.applyCommission(responseDatacom));
            } finally {
                meterRegistry.timer("commission.apply.time")
                        .record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
            }

            int totalItems = responseDatacom.getListGroup().size();
            PaginationMetadata pagination = new PaginationMetadata(
                    request.getPage(),
                    request.getPageSize(),
                    totalItems
            );

            int startIndex = (request.getPage() - 1) * request.getPageSize();
            int endIndex = Math.min(startIndex + request.getPageSize(), totalItems);

            List<Group> paginatedList = responseDatacom.getListGroup().subList(startIndex, endIndex);

            BeanUtils.copyProperties(responseDatacom, response);
            response.getData().setListGroup(paginatedList);
            response.setPaginationMetadata(pagination);

            return response;
        }

        return response;
    }
}
