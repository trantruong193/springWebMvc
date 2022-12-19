package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.entities.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.springWebMvc.persistent.entities.OrderDetail} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO implements Serializable {
    private Long orderDetailId;
    private ProductDetailDTO productDetailDTO;
    private int quantity;
    private double price;

    public OrderDetailDTO(OrderDetail orderDetail) {
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.price = orderDetail.getPrice();
        this.quantity = orderDetail.getQuantity();
        this.productDetailDTO = new ProductDetailDTO(orderDetail.getProductDetail());
    }
}