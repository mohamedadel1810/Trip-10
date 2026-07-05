package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepo extends JpaRepository<Permission,Integer> {

    Optional<Permission> findByName(String name);



}
