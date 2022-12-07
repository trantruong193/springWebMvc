package com.example.springWebMvc.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long productDetailId;
    private String imgUrl;
    private String productDetailName;
    private String colorName;
    private String typeName;
    private int quantity;
    private double price;
}
