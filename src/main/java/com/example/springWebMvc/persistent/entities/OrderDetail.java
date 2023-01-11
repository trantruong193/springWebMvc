package com.example.springWebMvc.persistent.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderDetails")
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    private double price;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "productDetailId")
    @ToString.Exclude
    private ProductDetail productDetail;
    @ManyToOne
    @JoinColumn(name = "orderId")
    @ToString.Exclude
    private Order order;
}
