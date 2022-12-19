package com.example.springWebMvc.persistent.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@Builder
@Entity
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cusId;
    private String cusName;
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private Date birthday;
    private String phone;
    private String address;
    private String avatarUrl;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId",referencedColumnName = "userId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}
