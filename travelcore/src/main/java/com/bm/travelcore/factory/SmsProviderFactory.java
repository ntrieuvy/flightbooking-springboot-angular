package com.bm.travelcore.factory;

import com.bm.travelcore.strategy.SmsProviderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SmsProviderFactory {

    private final Map<String, SmsProviderStrategy> strategyMap;

    public SmsProviderFactory(List<SmsProviderStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        s -> s.getClass().getAnnotation(Service.class).value(),
                        Function.identity()
                ));
    }

    public SmsProviderStrategy getStrategy(String provider) {
        SmsProviderStrategy strategy = strategyMap.get(provider);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid provider: " + provider);
        }
        return strategy;
    }
}
