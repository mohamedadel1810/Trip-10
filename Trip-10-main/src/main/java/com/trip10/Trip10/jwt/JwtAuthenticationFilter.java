package com.trip10.Trip10.jwt;

import com.trip10.Trip10.config.AdminDetailsService;
import com.trip10.Trip10.repos.CustomerRepo;
import com.trip10.Trip10.repos.DriverRepo;
import com.trip10.Trip10.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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


        String email = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);


        if (email != null && role != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = resolveUserDetails(email, role);

            if (userDetails != null && jwtService.isTokenValid(token, email)) {

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
     */
    private UserDetails resolveUserDetails(String email, String role) {
        try {
            return switch (role) {
                case "ADMIN" -> adminDetailsService.loadUserByUsername(email);
                case "DRIVER" -> driverRepo.findDriverByEmail(email)
                        .map(driver -> (UserDetails) User.builder()
                                .username(driver.getEmail())
                                .password(driver.getPassword())
                                .authorities(List.of(new SimpleGrantedAuthority("ROLE_DRIVER")))
                                .build())
                        .orElse(null);
                case "CUSTOMER" -> customerRepo.findCustomerByEmail(email)
                        .map(customer -> (UserDetails) User.builder()
                                .username(customer.getEmail())
                                .password(customer.getPassword())
                                .authorities(List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                                .build())
                        .orElse(null);
                default -> null;
            };
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }


}
