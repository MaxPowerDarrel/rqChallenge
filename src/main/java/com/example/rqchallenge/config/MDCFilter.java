package com.example.rqchallenge.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class MDCFilter extends OncePerRequestFilter {
    public static final String CORRELATION_ID = "X-Correlation-ID";

    /**
     * add a correlation id to enable ease of triage in logs
     * if the request already has one, we honor and use it, otherwise we create a new one
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var correlationId = Optional.ofNullable(request.getHeader("X-Correlation-ID"))
                    .orElseGet(() -> UUID.randomUUID().toString());
            MDC.put(CORRELATION_ID, correlationId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID);
        }
    }
}
