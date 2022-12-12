package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.User;

public interface UserService {
    User getUserByUsername(String username);
    User getUserById(Long id);
    boolean save(User user);
}
