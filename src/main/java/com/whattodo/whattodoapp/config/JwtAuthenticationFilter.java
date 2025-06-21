package com.whattodo.whattodoapp.config;

import com.whattodo.whattodoapp.security.CustomUserDetailsService;
import com.whattodo.whattodoapp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Filters incoming HTTP requests to:
     * 1. Extract JWT token from the "Authorization" header.
     * 2. Validate the token and extract the username.
     * 3. Load user details using username.
     * 4. If valid, create an Authentication object and set it in the SecurityContext,
     *    enabling Spring Security to authorize the user for the current request.
     * This filter runs once per request and integrates JWT token validation with Spring Security.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // If header is missing or does not start with "Bearer ", continue filter chain without setting authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from the header by removing "Bearer " prefix
        jwt = authHeader.substring(7);

        // Extract username from JWT token using JwtService
        username = jwtService.extractUsername(jwt);

        // Proceed if username is present and no authentication is set yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load UserDetails from the database or other source
            var userDetails = userDetailsService.loadUserByUsername(username);

            // Validate JWT token against user details
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Set additional details from the HTTP request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Store the Authentication token in the SecurityContext for Spring Security to use
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain regardless of authentication outcome
        filterChain.doFilter(request, response);
    }
}