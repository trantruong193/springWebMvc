package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.entities.Role;
import com.example.springWebMvc.persistent.entities.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Data
public class CustomizeOAth2User implements OAuth2User {

    private OAuth2User oAuth2User;
    private Optional<User> user;
    public CustomizeOAth2User(OAuth2User oAuth2User, Optional<User> user) {
        this.oAuth2User = oAuth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.isPresent()){
            List<Role> roles = user.get().getRoles();
            List<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getAuthority().getAuthorityName()));
            });
            return authorities;
        }
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }
    public String getEmail(){ return oAuth2User.getAttribute("email");}

    public Long getUserId(){
        return user.map(User::getUserId).orElse(null);
    }

    public String getImg(){
        if (user.isPresent()){
            if (user.get().getCustomer()!=null)
                return user.get().getCustomer().getAvatarUrl();
        }
        return "";
    }
}
