package com.zaitsev.bankapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestId =
                UUID.randomUUID().toString();

        long start = System.currentTimeMillis();

        try {

            filterChain.doFilter(request, response);

        } finally {

            long duration =
                    System.currentTimeMillis() - start;

            log.info(
                    "REQUEST id={}, method={}, path={}, ip={}, status={}, duration={}ms",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    response.getStatus(),
                    duration
            );
        }
    }
}