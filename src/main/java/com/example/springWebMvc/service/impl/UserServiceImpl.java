package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.User;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User getUserById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepository.getUsersByEmail(email).isPresent();
    }

    @Override
    public boolean verify(String code) {
        User user = userRepository.getUserByVerifyCode(code);
        if (user!=null){
            user.setStatus(1);
            userRepository.save(user);
            return true;
        }else
            return false;
    }

    @Override
    public String resetPassword(String email) {
        String newPassword = RandomString.make(9);
        if (userRepository.getUsersByEmail(email).isPresent()){
            User user = userRepository.getUsersByEmail(email).get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return newPassword;
        }else
            return "";
    }

    @Override
    public User getUserByResetPasswordCode(String code) {
        return userRepository.getUserByResetPasswordCode(code);
    }
}
