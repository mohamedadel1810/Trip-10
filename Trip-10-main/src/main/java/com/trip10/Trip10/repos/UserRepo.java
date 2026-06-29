package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
