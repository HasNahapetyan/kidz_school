package com.example.kidz_school.filter;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kidz_school.entity.Role;
import com.example.kidz_school.security.CurrentUser;
import com.example.kidz_school.service.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private static final String USER_ID = "userId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String ROLE = "role";
    private static final String AUTHORITIES = "authorities";
    private static final String ENABLED = "enabled";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = (String) request.getSession().getAttribute("AuthorizationToken");
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = jwt.replace("Bearer ", "");
        try {
            JWTVerifier jwtVerifier = jwtService.jwtVerifier();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            CurrentUser currentUser = createCurrentUser(decodedJWT);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            currentUser, null, currentUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (JWTVerificationException ex) {
            request.getSession().invalidate();
        }
        filterChain.doFilter(request, response);
    }

    private CurrentUser createCurrentUser(DecodedJWT decodedJWT) {
        Claim authoritiesClaim = decodedJWT.getClaim(AUTHORITIES);
        List<String> authoritiesInString = authoritiesClaim.asList(String.class);
        List<SimpleGrantedAuthority> authorities =
                jwtService.authoritiesFromString(authoritiesInString);
        return CurrentUser.builder()
                .email(decodedJWT.getSubject())
                .id(Long.parseLong(String.valueOf(decodedJWT.getClaim(USER_ID))))
                .firstName(decodedJWT.getClaim(FIRST_NAME).asString())
                .lastName(decodedJWT.getClaim(LAST_NAME).asString())
                .phone(decodedJWT.getClaim(PHONE).asString())
                .role(Role.valueOf(decodedJWT.getClaim(ROLE).asString()))
                .enabled(Boolean.parseBoolean(decodedJWT.getClaim(ENABLED).asString()))
                .authorities(authorities)
                .build();
    }
}