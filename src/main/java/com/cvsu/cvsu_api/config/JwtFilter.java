package com.cvsu.cvsu_api.config;

import com.cvsu.cvsu_api.utl.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if (requestURI.matches("^/cvsu/login(/.*)?$") || requestURI.matches("^/cvsu/displayBldgAndRooms(/.*)?$")) {
            chain.doFilter(request, response);
            return;
        }


        String token = extractToken(request);
        System.out.println("Extracted Token: " + token);


        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.extractUserClaims(token);
            if (claims != null) {
                // ✅ Extract user profile details
                String fullname = claims.get("fullName", String.class);
                String rolename = claims.get("roleName", String.class);
                String position = claims.get("position", String.class);
                Long id = claims.get("id", Long.class);
                String username = claims.get("username", String.class);
                String password = claims.get("password", String.class);

                System.out.println("Authenticated User: " + username + ", Role: " + rolename);

                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(rolename));

                UserDetails userDetails = new User(username, password, authorities);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            System.out.println("Invalid or missing token.");
        }

        chain.doFilter(request, response);
    }


    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }



}
