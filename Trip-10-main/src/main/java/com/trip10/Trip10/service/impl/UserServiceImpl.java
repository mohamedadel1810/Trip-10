package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.entity.Permission;
import com.trip10.Trip10.entity.User;
import com.trip10.Trip10.repos.UserRepo;
import com.trip10.Trip10.service.UserService;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ApiResponse<List<User>> findAll() {
        return ApiResponse.success("users fetched successfully",userRepo.findAll());
    }

    @Override
    public ApiResponse<User> findById(int id) {
        return userRepo.findById(id).map(user -> ApiResponse.success("user fetched successfully",user))
                .orElse(ApiResponse.notFound("user not found "+id));
    }

    @Override
    @Transactional
    public ApiResponse<User> create(User user) {

        User saved =userRepo.save(new User(user.getId(), user.getUsername(),user.getPermissionId()));
        return ApiResponse.success("user created successfully",saved);

    }

    @Override
    public ApiResponse<User> Update(int id, User user) {
        return userRepo.findById(id)
                .map(user1 -> {

                    user1.setUsername(user.getUsername());
                    user1.setPermissionId(user.getPermissionId());

                    User savedUser = userRepo.save(user1);

                    return ApiResponse.success(
                            "User updated successfully",
                            savedUser
                    );
                })
                .orElse(ApiResponse.notFound("User not found: " + id));
    }



    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        if (!userRepo.findById(id).isPresent()){
            return ApiResponse.notFound("user not found "+id);
        }
        userRepo.deleteById(id);
        return ApiResponse.success("user deleted successfully",null);
    }
}
