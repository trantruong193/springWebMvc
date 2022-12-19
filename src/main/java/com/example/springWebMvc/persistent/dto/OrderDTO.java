package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.PaymentMethod;
import com.example.springWebMvc.persistent.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.example.springWebMvc.persistent.entities.Order} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable{
    private Long orderId;
    @NotEmpty
    private String cusName;
    @NotEmpty
    private String phone;
    private String orderCode;
    @NotEmpty

    private String address;
    @NotEmpty
    @Email
    private String email;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private Date createTime;
    private OrderStatus status;
    private double totalPrice;
    private Long userId;
    List<OrderDetailDTO> orderDetailDTOList;
    public OrderDTO(Order order) {
        this.orderId = order.getOrderId();
        this.orderCode = order.getOrderCode();
        this.cusName = order.getCusName();
        this.phone = order.getPhone();
        this.address = order.getAddress();
        this.email = order.getEmail();
        this.paymentMethod = order.getPaymentMethod();
        this.createTime = order.getCreateTime();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.userId = order.getUserId();
        List<OrderDetailDTO> list = new ArrayList<>();
        if (order.getOrderDetails()!=null){
            order.getOrderDetails().forEach(orderDetail -> {
                list.add(new OrderDetailDTO(orderDetail));
            });
        }
        this.orderDetailDTOList = list;
    }
}