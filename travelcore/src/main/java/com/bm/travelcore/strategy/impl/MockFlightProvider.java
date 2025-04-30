package com.bm.travelcore.strategy.impl;

import com.bm.travelcore.dto.FlightSearchDatacomResDTO;
import com.bm.travelcore.dto.SearchFlightReqDTO;
import com.bm.travelcore.strategy.FlightProviderStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("mockflight")
@AllArgsConstructor
public class MockFlightProvider implements FlightProviderStrategy {

    @Override
    public Object searchFlights(SearchFlightReqDTO request) {
        try {
            String apiUrl = "http://localhost:3000/flights";
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<FlightSearchDatacomResDTO> response = restTemplate.postForEntity(
                    apiUrl,
                    request,
                    FlightSearchDatacomResDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling mock flight provider", e);
            return null;
        }
    }
}
