package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO implements Serializable {

    private Long cusId;
    @NotEmpty
    private String cusName;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    private Date birthday;
    @NotEmpty
    private String phone;
    private String email;
    @NotEmpty
    private String address;
    private String avatarUrl;
    private Long userId;
    private String userName;
    public CustomerDTO(Customer customer){
        this.cusId = customer.getCusId();
        this.cusName = customer.getCusName();
        this.birthday = customer.getBirthday();
        this.phone = customer.getPhone();
        this.email = customer.getUser().getEmail();
        this.address = customer.getAddress();
        this.avatarUrl = customer.getAvatarUrl();
        this.userId = customer.getUser().getUserId();
        this.userName = customer.getUser().getUsername();
    }
}
