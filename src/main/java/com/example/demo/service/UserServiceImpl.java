package com.example.demo.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.demo.dao.User;
import com.example.demo.dto.PagingDTO;
import com.example.demo.dto.UserPrincipal;
import com.example.demo.repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User create(User t) {
        // TODO Auto-generated method stub
        User existed = userRepository.findUserByEmail(t.getEmail());
        if (existed == null) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.INFO, "create user: "+t.getEmail());
            String pwd = hashPashword(t.getPassword());
            t.setPassword(pwd);
            return userRepository.save(t);
        }
        return null;
    }

    @Override
    public PagingDTO<User> findAll(Integer limit, Integer offset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User update(User t, String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public User retrieve(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserPrincipal login(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if(user != null && isValidPassword(password, user)) {
            user.setPassword("");
            return UserPrincipal.builder()
            .user(user)
            .build();
        }
        return null;
    }

    public boolean isValidPassword(String password, User user) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    public String hashPashword(String password) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(4));
        Logger.getLogger(UserServiceImpl.class.getName()).log(Level.INFO, "hash pwd: " + hash);
        return hash;
    }
}
