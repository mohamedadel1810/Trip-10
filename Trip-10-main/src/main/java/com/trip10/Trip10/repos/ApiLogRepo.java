package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiLogRepo extends JpaRepository<ApiLog, Integer> {
    List<ApiLog> findTop100ByOrderByIdDesc();
}
