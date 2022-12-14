package com.example.springWebMvc.persistent.entities;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.PaymentMethod;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long orderId;
    @Column(name = "orderCode")
    private String orderCode;
    @Column(name = "customerName")
    private String cusName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "paymentMethod")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @CreationTimestamp
    @Column(name = "orderDate",updatable = false)
    private Date createTime;
    @Column(name = "orderStatus")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "userId")
    private Long userId;
    private double totalPrice;

    @OneToMany(mappedBy = "order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderTracking> orderTracings;
}
