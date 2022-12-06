package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.User;

public interface UserService {
    User getUserByUsername(String username);
    boolean save(User user);
}
