package com.whattodo.whattodoapp.service;

import com.whattodo.whattodoapp.dto.LoginRequest;
import com.whattodo.whattodoapp.dto.RegisterRequest;
import com.whattodo.whattodoapp.dto.ChangeEmailRequest;
import com.whattodo.whattodoapp.dto.ChangePasswordRequest;
import com.whattodo.whattodoapp.model.User.User;
import com.whattodo.whattodoapp.model.User.UserRepository;
import com.whattodo.whattodoapp.security.CustomUserDetailsService;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Handles user registration by validating input and saving the new user to the database
    public ResponseEntity<?> register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Passwords do not match"));
        }
        if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Username or email already exists"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton("USER"));
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
    }

    // Handles user login by validating credentials and generating a JWT token
    public ResponseEntity<?> login(LoginRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        if (userDetails == null || !passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid username or password"));
        }

        String token = jwtService.generateToken(userDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // Changes the user's email address after validating the current password and checking for email uniqueness
    public ResponseEntity<?> changeEmail(ChangeEmailRequest request, CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid password."));
        }

        if (userRepository.existsByEmail(request.getNewEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "New email is already in use."));
        }

        user.setEmail(request.getNewEmail());
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "Email updated successfully."));
    }

   // Changes the user's password after validating the current password and ensuring new passwords match
    public ResponseEntity<?> changePassword(ChangePasswordRequest request, CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid current password."));
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "New passwords do not match."));
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "Password updated successfully."));
    }
}
