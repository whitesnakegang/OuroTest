package com.c102.ourotest.controller;
import com.c102.ourotest.dto.Order;
import com.c102.ourotest.dto.OrderCreateRequest;
import com.c102.ourotest.service.OrderService;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final OrderService orderService;

    public RestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/hello")
    @ApiState(state = State.COMPLETED)
    public String hello() {
        return "hello";
    }

    @GetMapping("/orders")
    @ApiState(state = State.COMPLETED)
    public List<Order> getOrders() {
        try {
            orderService.test1();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orderService.getAllOrders();
    }

    @PostMapping("/orders")
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<Order> createOrder(
            @RequestParam(required = false, defaultValue = "false") Boolean applyDiscount,
            @RequestParam(required = false, defaultValue = "0.0") Double discountRate,
            @RequestBody OrderCreateRequest request) {
        try {
            Order order = orderService.createOrder(request, applyDiscount, discountRate);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
