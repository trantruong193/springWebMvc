package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.User;

import java.util.List;

public interface UserService {
    User getUserByUsername(String username);
    User getUserById(Long id);
    boolean checkEmail(String email);
    boolean save(User user);
    boolean verify(String code);
    String resetPassword(String email);
    User getUserByResetPasswordCode(String code);

    User getUserByEmail(String email);
}
