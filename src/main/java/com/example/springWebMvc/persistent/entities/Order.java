package com.example.springWebMvc.persistent.entities;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Column(name = "orderDate")
    private Date createTime;
    @Column(name = "orderStatus")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "userId")
    private Long userId;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}
