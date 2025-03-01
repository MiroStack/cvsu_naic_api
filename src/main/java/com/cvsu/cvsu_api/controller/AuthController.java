package com.cvsu.cvsu_api.controller;

import com.cvsu.cvsu_api.entity.UserProfileEntity;
import com.cvsu.cvsu_api.model.AuthModel;
import com.cvsu.cvsu_api.model.UserProfileModel;
import com.cvsu.cvsu_api.serviceImp.AuthServiceImp;
import com.cvsu.cvsu_api.utl.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"https://cvsu-portal-weld.vercel.app", "http://localhost:5501"})
@RestController
@RequestMapping("cvsu")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthServiceImp authServiceImp;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthServiceImp authServiceImp) {
        this.jwtUtil = jwtUtil;
        this.authServiceImp = authServiceImp;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthModel model) {
        try {
            UserProfileModel user = authServiceImp.login(model.getUsername(), model.getPassword());
            if (user == null) {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }



    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        Claims claims = jwtUtil.extractUserClaims(token);
        if (claims == null) {
            return ResponseEntity.status(401).body("Invalid Token");
        }

        // ✅ Extract user profile details
        String fullname = claims.get("fullName", String.class);
        String rolename = claims.get("roleName", String.class);
        String position = claims.get("position", String.class);
        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String password = claims.get("password", String.class);


        // ✅ Return full user info
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("fullname", fullname);
        userInfo.put("rolename", rolename);
        userInfo.put("position", position);
        userInfo.put("id", id);
        userInfo.put("username", username);
        return ResponseEntity.ok(userInfo);
    }

}
