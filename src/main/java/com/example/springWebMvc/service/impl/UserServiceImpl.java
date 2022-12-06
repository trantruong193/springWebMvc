package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.User;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUsersByUsername(username).orElse(null);
    }
    @Override
    public boolean save(User user) {
        try {
            userRepository.save(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
