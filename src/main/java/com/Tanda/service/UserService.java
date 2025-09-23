package com.Tanda.service;

import com.Tanda.entity.Token;
import com.Tanda.entity.Role;
import com.Tanda.entity.User;
import com.Tanda.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Создаём Spring Security User, привязывая enabled/locked
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                    // username = email
                user.getPassword(),                 // already encoded
                user.isEnabled(),                   // enabled
                true,                               // accountNonExpired
                true,                               // credentialsNonExpired
                !user.isLocked(),                   // accountNonLocked
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }


    public User registerUser(User user) {
        // check if user with username or email already exist
        userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .ifPresent(existingUser -> {
                    throw new IllegalStateException("User already exists");
                });

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        Token confirmationToken = new Token(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        tokenService.save(confirmationToken);
        emailService.sendSimpleMail(user.getEmail(), token);
        System.out.println(token);

        return user;
    }

    public void enableUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public boolean confirmToken(String token) {
        Token confirmationToken = tokenService.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("User already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if(expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }else{
            confirmationToken.setConfirmedAt(LocalDateTime.now());
            tokenService.save(confirmationToken);
//TODO тут пздц крч скорее всего тут не работает enable true его надо починить и будет работать логин
            enableUser(confirmationToken.getUser());
            return true;
        }
    }
}
