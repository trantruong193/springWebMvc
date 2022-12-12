package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.dto.CustomizeUserDetails;
import com.example.springWebMvc.persistent.entities.User;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUsersByUsername(username).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException(username);
        return new CustomizeUserDetails(user);
    }
}
