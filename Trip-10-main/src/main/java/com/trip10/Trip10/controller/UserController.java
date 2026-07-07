package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.PermissionRequest;
import com.trip10.Trip10.entity.Permission;
import com.trip10.Trip10.entity.User;
import com.trip10.Trip10.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> findAll(){
        return userService.findAll().toResponseEntity();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getById(@PathVariable int id) {
        return userService.findById(id).toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@RequestBody User user) {
        return userService.create(user).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable int id, @RequestBody User user) {
        return userService.Update(id, user).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        return userService.deleteById(id).toResponseEntity();
    }



}
