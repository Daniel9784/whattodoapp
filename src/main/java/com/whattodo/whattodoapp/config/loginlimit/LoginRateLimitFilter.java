package com.whattodo.whattodoapp.config.loginlimit;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginRateLimitFilter extends HttpFilter {

    private final LoginRateLimiter loginRateLimiter;

    public LoginRateLimitFilter(LoginRateLimiter loginRateLimiter) {
        this.loginRateLimiter = loginRateLimiter;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if ("/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String ip = request.getRemoteAddr();
            Bucket bucket = loginRateLimiter.resolveBucket(ip);

            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response);
            } else {
                response.setStatus(429);
                response.getWriter().write("Too many login attempts. Please try again later.");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
