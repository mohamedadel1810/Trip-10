package com.trip10.Trip10.jwt;

import com.trip10.Trip10.config.AdminDetailsService;
import com.trip10.Trip10.repos.CustomerRepo;
import com.trip10.Trip10.repos.DriverRepo;
import com.trip10.Trip10.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AdminDetailsService adminDetailsService;
    private final DriverRepo driverRepo;
    private final CustomerRepo customerRepo;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = authHeader.substring(7);

        String subject;
        String role;
        try {
            subject = jwtService.extractUsername(token);
            role = jwtService.extractRole(token);
        } catch (JwtException | IllegalArgumentException e) {
            // Expired, malformed, or otherwise unparseable token — treat this
            // request as unauthenticated rather than crashing. Whether that's
            // acceptable is then decided normally by the security rules for
            // the target endpoint (permitAll vs authenticated).
            filterChain.doFilter(request, response);
            return;
        }


        if (subject != null && role != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = resolveUserDetails(subject, role);

            if (userDetails != null && jwtService.isTokenValid(token, subject)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Looks the token's subject up in the table matching its role claim, so a
     * driver/customer token is verified against a live row in its own table
     * (not the admin table) — revoked/deleted accounts stop working immediately
     * instead of staying valid until token expiry.
     *
     * ADMIN tokens carry the admin's email as subject (AdminDetailsService
     * follows Spring Security's username-based UserDetailsService contract,
     * needed for the password-check step at login time). DRIVER/CUSTOMER
     * tokens carry the numeric id instead — looking them up by id rather than
     * email means changing your own email via self-update can't invalidate
     * your existing token.
     */
    private UserDetails resolveUserDetails(String subject, String role) {
        try {
            return switch (role) {
                case "ADMIN" -> adminDetailsService.loadUserByUsername(subject);
                case "DRIVER" -> driverRepo.findById(Integer.parseInt(subject))
                        .map(driver -> (UserDetails) User.builder()
                                .username(String.valueOf(driver.getId()))
                                .password(driver.getPassword())
                                .authorities(List.of(new SimpleGrantedAuthority("ROLE_DRIVER")))
                                .build())
                        .orElse(null);
                case "CUSTOMER" -> customerRepo.findById(Integer.parseInt(subject))
                        .map(customer -> (UserDetails) User.builder()
                                .username(String.valueOf(customer.getId()))
                                .password(customer.getPassword())
                                .authorities(List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                                .build())
                        .orElse(null);
                default -> null;
            };
        } catch (UsernameNotFoundException | NumberFormatException e) {
            return null;
        }
    }


}
