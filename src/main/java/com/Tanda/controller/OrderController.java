package com.Tanda.controller;

import com.Tanda.entity.Order;
import com.Tanda.entity.User;
import com.Tanda.service.OrderService;
import com.Tanda.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(orderService.checkout(user));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }
}
