package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.entities.OrderTracking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.example.springWebMvc.persistent.entities.OrderTracking} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTrackingDTO implements Serializable {
    private OrderStatus orderStatus;
    private String description;
    private String orderDetail;
    private Date date;
    public OrderTrackingDTO(OrderTracking tracking){
        this.orderStatus = tracking.getOrderStatus();
        this.description = tracking.getDescription();
        this.date = tracking.getDate();
    }
}