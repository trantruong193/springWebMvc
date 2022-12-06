package com.example.springWebMvc.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailDTO {
    private Long productDetailId;
    @NotNull
    @Min(0)
    @Max(100)
    private double discount;
    @NotNull
    @Min(0)
    private int quantity;
    @NotNull
    private Long proId;
    @NotNull
    private Long colorId;
    @NotNull
    private Long typeId;
    private String colorName;
    private String typeName;
}
