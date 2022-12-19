package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.ProductStatus;
import com.example.springWebMvc.persistent.entities.Category;
import com.example.springWebMvc.persistent.entities.Producer;
import com.example.springWebMvc.persistent.entities.Product;
import com.example.springWebMvc.persistent.entities.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO implements Serializable {
    private Long proId;
    @NotEmpty
    private String proName;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private Long producerId;
    @Min(0)
    @NotNull
    private double basePrice;
    @Min(0)
    @NotNull
    @Max(100)
    private Float discount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date importDate;
    private String imgUrl;
    private String imgUrl1;
    private String imgUrl2;
    private String imgUrl3;
    private Long catId;
    private Long catalogId;
    @NotEmpty
    @Length(max = 1000,min = 10)
    private String description;
    @NotEmpty
    @Length(max = 1000,min = 10)
    private String description1;
    @NotEmpty
    @Length(max = 1000,min = 10)
    private String description2;
    private List<ProductDetailDTO> productDetailDTOS;
    public ProductDTO(Product product){
        this.proId = product.getProId();
        this.proName = product.getProName();
        this.basePrice = product.getBasePrice();
        List<ProductDetail> details = product.getProductDetails();
        List<ProductDetailDTO> detailDTOS = new ArrayList<>();
        details.forEach(detail->{
            detailDTOS.add(new ProductDetailDTO(detail));
        });
        detailDTOS.sort(Comparator.comparing(ProductDetailDTO::getTypeName));
        this.productDetailDTOS = detailDTOS;
    }
}