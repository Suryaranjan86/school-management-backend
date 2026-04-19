package com.srs.school.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to extract and store X-School-Id header in thread-local storage
 * X-School-Id header is mandatory for all API requests except User controller endpoints
 */
@Component
public class SchoolIdFilter implements Filter {
    
    private static final ThreadLocal<String> schoolIdHolder = new ThreadLocal<>();
    
    // Endpoints that don't require X-School-Id header
    private static final String[] EXCLUDED_PATHS = {
        "/api/users/login",
        "/api/users/register",
        "/api/users/forgot-password",
        "/api/users/reset-password",
            "/api/schools"
    };
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestPath = httpRequest.getRequestURI();
        System.out.println("METHOD = " + httpRequest.getMethod());
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        // Check if the request path is excluded from X-School-Id validation
        if (!isExcludedPath(requestPath)) {
            String schoolId = httpRequest.getHeader("X-School-Id");
            
            // X-School-Id header is mandatory for all protected API requests
            if (schoolId == null || schoolId.isEmpty()) {
                return;
            }
            
            schoolIdHolder.set(schoolId);
        }
        
        try {
            chain.doFilter(request, response);
        } finally {
            schoolIdHolder.remove();
        }
    }
    
    /**
     * Check if the request path is excluded from X-School-Id validation
     */
    private boolean isExcludedPath(String requestPath) {
        for (String excludedPath : EXCLUDED_PATHS) {
            if (requestPath.equals(excludedPath) || requestPath.startsWith(excludedPath + "/")) {
                return true;
            }
        }
        return false;
    }
    
    public static String getSchoolId() {
        return schoolIdHolder.get();
    }
    
    public static void setSchoolId(String schoolId) {
        schoolIdHolder.set(schoolId);
    }
}

