package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.entities.Review;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.example.springWebMvc.persistent.entities.Review} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO implements Serializable {
    private Long id;
    @NotEmpty
    private String cusName;
    @NotEmpty
    private String cusEmail;
    @NotEmpty
    private String message;
    @NotNull
    private int rate;
    private Date createDate;
    private boolean status;
    private String proName;
    private String shopReply;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.cusName = review.getCusName();
        this.cusEmail = review.getCusEmail();
        this.message = review.getMessage();
        this.rate = review.getRate();
        this.createDate = review.getCreateDate();
        this.status = review.isStatus();
        this.proName = review.getProduct().getProName();
        this.shopReply = review.getShopReply();
    }
}