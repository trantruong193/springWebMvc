package com.example.springWebMvc.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customerName")
    private String cusName;
    @Column(name = "customerEmail")
    private String cusEmail;
    @Column(name = "messages",length = 1000)
    private String message;
    @Column(name = "rate")
    private int rate;
    @Column(name = "createDate")
    @CreationTimestamp
    private Date createDate;
    @Column(name = "status")
    private boolean status;
    @Column(name = "shopReply")
    private String shopReply;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
