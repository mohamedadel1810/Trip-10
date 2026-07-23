package com.trip10.Trip10.config;

import com.trip10.Trip10.entity.Admin;
import com.trip10.Trip10.repos.AdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException{

        Admin admin =adminRepo.findAdminByEmail(email).orElseThrow(()->
                new UsernameNotFoundException("admin not found"));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
    }
}
