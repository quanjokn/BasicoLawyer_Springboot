package com.fptu.hk7.basicolawer.config.JWT;

import com.fptu.hk7.basicolawer.config.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final List<String> PUBLIC_AUTH_SERVICE_APIS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/auth/login",
            "/auth/register",
            "/auth/forgot-password",
            "/api/staff-profiles/**",
            "/api/forms/vnpay/return",
            "/api/forms",
            "/api/payment/vnpay/return/**",
            "/api/staff-profiles/**",
            "/uploads/**",
            "/api/assets/**",
            "/api/posts",
            "/api/categories",
            "/test/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean checkIsPublicAPI(String uri) {
        return PUBLIC_AUTH_SERVICE_APIS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("[Filter] Incoming request: " + requestURI + ", full URL: " + request.getRequestURL());
        boolean isPublicAPI = checkIsPublicAPI(requestURI);
        System.out.println("[Filter] isPublicAPI? " + isPublicAPI);
        if (isPublicAPI) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.getToken(request);
        if (token == null) {
            resolver.resolveException(request, response, null, new AuthException("JWT token is missing!"));
            return;
        }

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            resolver.resolveException(request, response, null, new AuthException("JWT token has expired!"));
        } catch (MalformedJwtException e) {
            resolver.resolveException(request, response, null, new AuthException("Invalid JWT token format!"));
        } catch (Exception e) {
            resolver.resolveException(request, response, null, new AuthException("JWT token processing error: " + e.getMessage()));
        }
    }
}
