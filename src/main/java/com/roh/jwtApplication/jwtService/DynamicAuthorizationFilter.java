package com.roh.jwtApplication.jwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class DynamicAuthorizationFilter extends OncePerRequestFilter {

    private final ApiAuthorizationService apiAuthorizationService;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public DynamicAuthorizationFilter(
            ApiAuthorizationService apiAuthorizationService, CustomAccessDeniedHandler accessDeniedHandler) {

        this.apiAuthorizationService = apiAuthorizationService;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String httpMethod = request.getMethod();
        String requestUri = request.getRequestURI();

        // 1. Find required permission for this API
        String requiredPermission =
                apiAuthorizationService.getRequiredPermission(
                        httpMethod,
                        requestUri
                );

        // 2. No permission mapping found
        if (requiredPermission == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Get authenticated user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // 4. User is not authenticated
        if (authentication == null ||
                !authentication.isAuthenticated()) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 5. Check whether user has required permission
        boolean hasPermission =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals(requiredPermission)
                        );

        // 6. Permission denied
        if (!hasPermission) {

            accessDeniedHandler.handle(
                    request,
                    response,
                    new AccessDeniedException(
                            "You don't have permission to access this resource"
                    )
            );

            return;
        }

        // 7. Permission granted
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String uri = request.getRequestURI();

        return uri.equals("/auth/login")
                || uri.equals("/auth/register");
    }
}
