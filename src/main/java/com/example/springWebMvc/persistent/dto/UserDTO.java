package com.example.springWebMvc.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    @NotEmpty
    @Min(3)
    @Max(20)
    private String username;
    @NotEmpty
    @Min(3)
    @Max(20)
    private String password;
    private int status;
}
