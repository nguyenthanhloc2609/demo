package com.example.demo.service;

import com.example.demo.dao.User;
import com.example.demo.dto.UserPrincipal;

public interface IUserService extends IBaseService<User>{
    public UserPrincipal login(String email, String password);
    
    
}
