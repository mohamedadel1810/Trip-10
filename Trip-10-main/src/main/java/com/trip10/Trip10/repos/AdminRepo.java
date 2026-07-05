package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin,Integer> {


    Optional<Admin> findAdminByEmail(String email);
}
