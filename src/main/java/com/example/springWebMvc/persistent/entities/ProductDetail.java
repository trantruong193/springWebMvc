package com.example.springWebMvc.persistent.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productDetail")
public class ProductDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDetailId;
    @Column(nullable = false)
    private double discount;
    @Column(nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "productId",referencedColumnName = "proId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "colorId",referencedColumnName = "colorId")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "typeId",referencedColumnName = "typeId")
    private Type type;
    @OneToMany(mappedBy = "productDetail")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderDetail> orderDetails;
}
