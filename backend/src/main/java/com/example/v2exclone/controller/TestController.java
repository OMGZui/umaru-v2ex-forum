package com.example.v2exclone.controller;

import com.example.v2exclone.entity.User;
import com.example.v2exclone.service.UserService;
import com.example.v2exclone.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/generate-token/{userId}")
    public ResponseEntity<?> generateTestToken(@PathVariable Long userId) {
        try {
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String token = jwtUtil.generateToken(user.getId(), user.getUsername());
                
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", user.getUsername());
                response.put("userId", user.getId());
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
