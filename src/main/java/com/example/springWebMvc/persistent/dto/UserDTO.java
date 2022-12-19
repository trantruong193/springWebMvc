package com.example.springWebMvc.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

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
    private String verifyCode;
}
