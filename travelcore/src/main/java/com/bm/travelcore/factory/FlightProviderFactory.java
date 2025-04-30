package com.bm.travelcore.factory;

import com.bm.travelcore.strategy.FlightProviderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FlightProviderFactory {

    private final Map<String, FlightProviderStrategy> providerMap;

    @Autowired
    public FlightProviderFactory(List<FlightProviderStrategy> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(
                        s -> s.getClass().getAnnotation(Service.class).value(),
                        Function.identity()
                ));
    }

    public FlightProviderStrategy getStrategy(String providerName) {
        FlightProviderStrategy provider = providerMap.get(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Invalid flight provider: " + providerName);
        }
        return provider;
    }
}