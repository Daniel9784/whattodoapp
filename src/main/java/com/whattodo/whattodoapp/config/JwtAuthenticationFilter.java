package com.whattodo.whattodoapp.config;

import com.whattodo.whattodoapp.service.JwtService;
import com.whattodo.whattodoapp.model.UserRepository;
import com.whattodo.whattodoapp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    /**
     * This method is called for every request to check if the JWT token is present and valid.
     * If valid, it sets the authentication in the security context.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Extract the JWT token from the Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        // Check if the Authorization header is present and starts with "Bearer"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If not, continue with the filter chain without setting authentication
            filterChain.doFilter(request, response);
            return;
        }
        // If the header is valid, extract the JWT token by removing "Bearer"
        jwt = authHeader.substring(7);

        // Extract username from the JWT token
        username = jwtService.extractUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Fetch user details from the database
            User user = userRepository.findByUsername(username).orElse(null);


            // Checking if the extracted user exists and if the JWT token is valid
            if (user != null && jwtService.isTokenValid(jwt, username)) {

                // Create an authentication token with user details and set it in the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
