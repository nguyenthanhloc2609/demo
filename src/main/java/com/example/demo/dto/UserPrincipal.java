package com.example.demo.dto;

import com.example.demo.dao.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPrincipal {
    private User user;
    private String accessToken;
    private String refreshToken;
}
