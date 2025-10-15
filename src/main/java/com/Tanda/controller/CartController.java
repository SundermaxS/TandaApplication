package com.Tanda.controller;

import com.Tanda.entity.Cart;
import com.Tanda.entity.User;
import com.Tanda.repository.UserRepository;
import com.Tanda.service.CartService;
import com.Tanda.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final UserService userService;

    @GetMapping
    public Cart getCart(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User user = userService.findUser(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartService.getUserCart(user);
    }


    @PostMapping("/add")
    public Cart addItem(@AuthenticationPrincipal User user,
                        @RequestParam Long productId,
                        @RequestParam int quantity) {
        return cartService.addProduct(user, productId, quantity);
    }

    @PutMapping("/update/{itemId}")
    public Cart updateItem(@AuthenticationPrincipal User user,
                           @PathVariable Long itemId,
                           @RequestParam int quantity) {
        return cartService.updateItem(user, itemId, quantity);
    }

    @DeleteMapping("/remove/{itemId}")
    public Cart removeItem(@AuthenticationPrincipal User user,
                           @PathVariable Long itemId) {
        return cartService.removeItem(user, itemId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
    }
}
