package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.entity.User;

import java.util.List;

public interface UserService {

    ApiResponse<List<User>> findAll();

    ApiResponse<User> findById(int id);

    ApiResponse<User> create(User user);

    ApiResponse<User> Update(int id,User user);

    ApiResponse<Void>deleteById(int id);

}
