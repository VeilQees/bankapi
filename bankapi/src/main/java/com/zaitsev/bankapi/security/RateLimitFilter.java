package com.zaitsev.bankapi.security;

import io.github.bucket4j.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter
        extends OncePerRequestFilter {

    private final Map<String, Bucket> cache =
            new ConcurrentHashMap<>();

    private Bucket createBucket(){

        Bandwidth limit = Bandwidth.simple(
                50,
                Duration.ofMinutes(1)
        );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")){

            filterChain.doFilter(request, response);

            return;
        }


        String ip = request.getRemoteAddr();

        Bucket bucket =
                cache.computeIfAbsent(
                        ip,
                        k -> createBucket()
                );

        if(bucket.tryConsume(1)){

            filterChain.doFilter(
                    request,
                    response
            );

        } else {

            response.setStatus(429);

            response.getWriter().write(
                    "Too many requests"
            );
        }
    }
}