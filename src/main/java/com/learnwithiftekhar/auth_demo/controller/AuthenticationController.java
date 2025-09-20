package com.learnwithiftekhar.auth_demo.controller;

import com.learnwithiftekhar.auth_demo.entity.User;
import com.learnwithiftekhar.auth_demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    // Регистрация нового пользователя
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered. Please check your email to confirm.");
    }

    // Подтверждение токена
    @GetMapping("/confirmToken")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {
        boolean result = userService.confirmToken(token);
        if (result) {
            return ResponseEntity.ok("Email confirmed successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }
}

