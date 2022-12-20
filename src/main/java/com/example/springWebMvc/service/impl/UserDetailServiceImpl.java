package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.dto.CustomizeUserDetails;
import com.example.springWebMvc.persistent.entities.User;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailServiceImpl.class);
    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.trace("Enter loadUserByUsername() method.");
        LOGGER.debug("Authenticating User : " + username);
        User user = userRepository.getUsersByUsername(username).orElse(null);
        if (user == null){
            LOGGER.error("Authentication failed.");
            throw new UsernameNotFoundException(username);
        }
        LOGGER.info("User authenticated successfully.");
        return new CustomizeUserDetails(user);
    }
}
