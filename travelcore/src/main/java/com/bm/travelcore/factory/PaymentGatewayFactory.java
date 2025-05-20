package com.bm.travelcore.factory;

import com.bm.travelcore.model.enums.PaymentType;
import com.bm.travelcore.strategy.PaymentGatewayStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentGatewayFactory {
    private final List<PaymentGatewayStrategy> gateways;

    public PaymentGatewayFactory(List<PaymentGatewayStrategy> gateways) {
        this.gateways = gateways;
    }

    public PaymentGatewayStrategy getGateway(PaymentType type) {
        return gateways.stream()
                .filter(g -> g.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Payment method not supported"));
    }
}
