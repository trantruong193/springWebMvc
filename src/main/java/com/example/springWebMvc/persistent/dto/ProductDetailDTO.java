package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.entities.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailDTO implements Serializable {
    private Long productDetailId;
    @NotNull
    @Min(0)
    @Max(100)
    private double discount;
    @NotNull
    @Min(0)
    private int quantity;
    private Long proId;
    @NotNull
    private Long colorId;
    @NotNull
    private Long typeId;
    private String productName;
    private String colorName;
    private String typeName;
    public ProductDetailDTO(ProductDetail productDetail){
        this.productDetailId = productDetail.getProductDetailId();
        this.productName = productDetail.getProduct().getProName();
        this.colorName = productDetail.getColor().getColorName();
        this.typeName = productDetail.getType().getTypeName();
        this.discount = productDetail.getDiscount();
        this.quantity = productDetail.getQuantity();
    }
}
