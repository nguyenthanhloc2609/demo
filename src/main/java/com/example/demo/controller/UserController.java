package com.example.demo.controller;

import com.example.demo.dao.User;
import com.example.demo.dto.Error;
import com.example.demo.dto.UserPrincipal;
import com.example.demo.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        UserPrincipal userPrincipal = userService.login(user.getEmail(), user.getPassword());
        if (userPrincipal != null)
            return ResponseEntity.ok(userPrincipal);
        else
            return ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST.value(), "Sai email hoặc mật khẩu"));
    }

    @PostMapping("/new-user")
    public ResponseEntity<?> create(@RequestBody User user) {
        User newUser = userService.create(user);
        if (newUser != null)
            return ResponseEntity.ok(newUser);
        else
            return ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST.value(), "Lỗi khi tạo user"));
    }
}
