package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.entities.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.example.springWebMvc.persistent.entities.Message} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO implements Serializable {
    private Long id;
    @NotEmpty
    @Length(max = 255)
    private String cusName;
    @NotEmpty
    @Length(max = 255)
    private String cusEmail;
    @NotEmpty
    @Length(max = 255)
    private String phone;
    @NotEmpty
    @Length(max = 1000)
    private String message;
    private Date createDate;
    private boolean status;
    private Long userId;
    public MessageDTO(Message message) {
        this.id = message.getId();
        this.cusName = message.getCusName();
        this.cusEmail = message.getCusEmail();
        this.phone = message.getPhone();
        this.message = message.getMessage();
        this.createDate = message.getCreateDate();
        this.status = message.isStatus();
    }
}