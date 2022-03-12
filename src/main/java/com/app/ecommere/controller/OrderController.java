package com.app.ecommere.controller;

import com.app.ecommere.payload.response.OrderResponse;
import com.app.ecommere.security.CustomUserDetail;
import com.app.ecommere.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("api/order")
    public OrderResponse createOrder(@AuthenticationPrincipal CustomUserDetail customUserDetail) {

        return orderService.createOrder(customUserDetail.getUserID());
    }

    @GetMapping("api/order")
    public OrderResponse getOrder(@AuthenticationPrincipal CustomUserDetail customUserDetail) {

        return orderService.getAllOrderByUserId(customUserDetail.getUserID());
    }
}
