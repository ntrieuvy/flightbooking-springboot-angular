package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.Airline;
import com.bm.travelcore.repository.AirlineRepository;
import com.bm.travelcore.service.AirlineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    @Override
    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }
}
