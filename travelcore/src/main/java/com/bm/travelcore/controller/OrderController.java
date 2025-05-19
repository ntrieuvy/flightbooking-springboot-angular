package com.bm.travelcore.controller;

import com.bm.travelcore.dto.OrderResDTO;
import com.bm.travelcore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResDTO>> getUserOrders(@RequestParam(required = false) String userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderResDTO> getOrderDetails(@PathVariable Long orderId) {
//        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
//    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}