package com.example.springWebMvc.persistent.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "color")
public class Color implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colorId")
    private Long colorId;
    @Column(name = "colorName")
    private String colorName;
    @Column(name = "colorCode")
    private String colorCode;
    @OneToMany(mappedBy = "color")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ProductDetail> productDetailList;
}
