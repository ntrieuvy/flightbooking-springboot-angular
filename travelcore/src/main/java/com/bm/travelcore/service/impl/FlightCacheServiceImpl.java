package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.Airport;
import com.bm.travelcore.model.AirportGroup;
import com.bm.travelcore.model.Commission;
import com.bm.travelcore.repository.AirportRepository;
import com.bm.travelcore.repository.CommissionRepository;
import com.bm.travelcore.service.FlightCacheService;
import com.bm.travelcore.utils.constant.AppConstant;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class FlightCacheServiceImpl implements FlightCacheService {

    private final AirportRepository airportRepository;
    private final CommissionRepository commissionRepository;

    @Override
    @Cacheable(value = AppConstant.AIRPORT_GROUP_CACHE, key = "#startPointCode + '-' + #endPointCode", unless = "#result == null")
    public AirportGroup findAirportGroupForFlight(String startPointCode, String endPointCode) {
        Airport startAirport = airportRepository.findByCode(startPointCode);
        Airport endAirport = airportRepository.findByCode(endPointCode);

        if (startAirport != null && endAirport != null &&
                startAirport.getAirportGroup() != null &&
                Objects.equals(startAirport.getAirportGroup(), endAirport.getAirportGroup())) {
            return startAirport.getAirportGroup();
        }

        return null;
    }

    @Override
    @Cacheable(value = AppConstant.COMMISSION_CACHE, key = "#userId + '-' + #airlineCode + '-' + #airportGroupId", unless = "#result == null")
    public Commission getCommission(Long userId, String airlineCode, Long airportGroupId) {
        return commissionRepository.findByUserIdAndAirlineCodeAndAirportGroupId(
                userId, airlineCode, airportGroupId);
    }

}
