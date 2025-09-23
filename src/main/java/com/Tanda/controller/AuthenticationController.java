package com.Tanda.controller;

import com.Tanda.entity.User;
import com.Tanda.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(UserService userService,
                                    AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered. Please check your email to confirm.");
    }

    @GetMapping("/confirmToken")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {
        boolean result = userService.confirmToken(token);
        if (result) return ResponseEntity.ok("Email confirmed successfully!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            // Устанавливаем аутентификацию в контекст (если нужна сессия)
            SecurityContextHolder.getContext().setAuthentication(auth);

            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("Logged out");
    }
}


