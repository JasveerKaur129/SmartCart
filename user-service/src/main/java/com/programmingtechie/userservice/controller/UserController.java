package com.programmingtechie.userservice.controller;


import com.programmingtechie.userservice.dto.JwtResponse;
import com.programmingtechie.userservice.model.User;
import com.programmingtechie.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;  // BCryptPasswordEncoder for password hashing

    // Signup endpoint to register a user
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        // Hash the password before saving it
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role as 'customer' if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");  // Default role
        }

        userRepository.save(user);
        return "User created successfully";
    }

    // Login endpoint to authenticate and generate a JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        System.out.println("Entered password: " + password);
        System.out.println("Stored hashed password: " + user.getPassword());

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {  // Compare hashed password
            // Generate a JWT token containing the username and roles
            String token = jwtUtil.generateToken(user.getUsername(), Collections.singletonList(user.getRole()));
            return ResponseEntity.ok().body(new JwtResponse(token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");  // Return error message if invalid
    }
}
