package com.example.springWebMvc.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    private Long productDetailId;
    private int quantity;
    private double price;
    private double discount;
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;
}
