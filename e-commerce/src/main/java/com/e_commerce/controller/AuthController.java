package com.e_commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Map<String, Object> response = new HashMap<>();
        
        if ("admin".equals(username) && "password".equals(password)) {
            response.put("success", true);
            response.put("message", "Login i suksesshëm");
            response.put("token", "admin-token-" + System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Username ose password i gabuar");
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logout i suksesshëm");
        return ResponseEntity.ok(response);
    }
}
