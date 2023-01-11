package com.example.springWebMvc.persistent.entities;

import com.example.springWebMvc.persistent.OrderStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order-tracking")
public class OrderTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackingId;
    @Column(name = "orderStatus",nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "description")
    private String description;
    private String orderDetail;
    @Column(name = "currentDate")
    @CreationTimestamp
    private Date date;

    @ManyToOne
    @JoinColumn(name = "orderId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;
}

