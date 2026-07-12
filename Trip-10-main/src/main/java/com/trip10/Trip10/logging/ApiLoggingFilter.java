package com.trip10.Trip10.logging;

import com.trip10.Trip10.entity.ApiLog;
import com.trip10.Trip10.repos.ApiLogRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private final ApiLogRepo apiLogRepo;

    private static final Pattern SENSITIVE_FIELD_PATTERN = Pattern.compile(
            "\"(password|token|otpCode|otp_code)\"\\s*:\\s*\"[^\"]*\"",
            Pattern.CASE_INSENSITIVE
    );

    public ApiLoggingFilter(ApiLogRepo apiLogRepo) {
        this.apiLogRepo = apiLogRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, 10240);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            saveLog(wrappedRequest, wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void saveLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        try {
            ApiLog log = new ApiLog();
            log.setHttpMethod(request.getMethod());
            log.setEndpoint(request.getRequestURI());
            log.setRequestBody(mask(new String(request.getContentAsByteArray(), StandardCharsets.UTF_8)));
            log.setResponseBody(mask(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8)));
            log.setStatusCode(response.getStatus());
            log.setPerformedBy(currentPrincipal());
            log.setIpAddress(request.getRemoteAddr());
            log.setCreatedAt(LocalDateTime.now());
            apiLogRepo.save(log);
        } catch (Exception ignored) {
            // logging must never break the actual request
        }
    }

    private String currentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "anonymous";
    }

    private String mask(String body) {
        if (body == null || body.isBlank()) return body;
        return SENSITIVE_FIELD_PATTERN.matcher(body).replaceAll("\"$1\":\"***MASKED***\"");
    }
}
