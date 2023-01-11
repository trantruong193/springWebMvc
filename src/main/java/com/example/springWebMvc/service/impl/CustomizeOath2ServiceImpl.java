package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.dto.CustomizeOAth2User;
import com.example.springWebMvc.persistent.entities.Authority;
import com.example.springWebMvc.persistent.entities.Role;
import com.example.springWebMvc.persistent.entities.User;
import com.example.springWebMvc.repository.RoleRepository;
import com.example.springWebMvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomizeOath2ServiceImpl extends DefaultOAuth2UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        // check if email has been used
        Optional<User> user = userRepository.getUsersByEmail(email);
        // case email has not been used, create new account
        if (user.isEmpty()){
            User user1 = new User();
            // generate new account
            String username = "";
            if (email != null){
                int index = email.indexOf("@");
                username = email.substring(0,index);
            }
            String random;
            boolean condition = true;
            do {
                random = UUID.randomUUID().toString().trim().replace("-","").substring(0,6);
                // check if new account exist
                if (userRepository.getUsersByUsername(username+random).isEmpty()){
                    condition = false;
                }
            }while (condition);
            // save new user
            username += random;
            user1.setUsername(username);
            String password = UUID.randomUUID().toString().substring(0,9);
            user1.setPassword(new BCryptPasswordEncoder().encode(password));
            user1.setEmail(email);
            user1.setStatus(1);
            User user2 = userRepository.save(user1);
            // set role for new user, default role is ROLE_CUSTOMER
            Role role = new Role();
            role.setUser(user2);
            role.setAuthority(Authority.builder().authorityId(3L).authorityName("ROLE_CUSTOMER").build());
            roleRepository.save(role);
            // get user has been created
            user = userRepository.getUsersByEmail(email);
        }
        // add user existed to authentication
        return new CustomizeOAth2User(oAuth2User,user);
    }
}
