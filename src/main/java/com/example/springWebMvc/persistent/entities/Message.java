package com.example.springWebMvc.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId")
    private Long id;
    @Column(name = "customerName",nullable = false)
    private String cusName;
    @Column(name = "customerEmail",nullable = false)
    private String cusEmail;
    @Column(name = "customerPhone")
    private String phone;
    @Column(name = "messageContent",length = 1000)
    private String message;
    @CreationTimestamp
    @Column(name = "createDate")
    private Date createDate;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
