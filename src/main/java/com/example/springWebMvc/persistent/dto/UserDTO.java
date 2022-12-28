package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Long userId;
    @NotEmpty
    @Min(3)
    @Max(20)
    private String username;
    @NotEmpty
    @Min(3)
    @Max(20)
    private String password;
    @NotEmpty
    @Email
    private String email;
    private int status;
    private List<String> roles;
    private String verifyCode;
    public UserDTO(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.status = user.getStatus();
        List<String> r = new ArrayList<>();
        if (!user.getRoles().isEmpty()){
            user.getRoles().forEach(role -> {
                r.add(role.getAuthority().getAuthorityName());
            });
            this.roles = r;
        }
    }
}
