package com.Tanda.controller;

import com.Tanda.entity.Cart;
import com.Tanda.entity.User;
import com.Tanda.service.CartService;
import com.Tanda.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final UserService userService;

    @GetMapping
    public Cart getCart(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartService.getUserCart(user);
    }


    @PostMapping("/add")
    public Cart addItem(@AuthenticationPrincipal UserDetails principal,
                        @RequestParam Long productId,
                        @RequestParam int quantity) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartService.addProduct(user, productId, quantity);
    }

    @PutMapping("/update/{itemId}")
    public Cart updateItem(@AuthenticationPrincipal UserDetails principal,
                           @PathVariable Long itemId,
                           @RequestParam int quantity) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartService.updateItem(user, itemId, quantity);
    }

    @DeleteMapping("/remove/{itemId}")
    public Cart removeItem(@AuthenticationPrincipal UserDetails principal,
                           @PathVariable Long itemId) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartService.removeItem(user, itemId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartService.clearCart(user);
    }
}
