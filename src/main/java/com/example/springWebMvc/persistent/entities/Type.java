package com.example.springWebMvc.persistent.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "types")
public class Type implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeId")
    private Long typeId;
    @Column(name = "typeName")
    private String typeName;
    @OneToMany(mappedBy = "type")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ProductDetail> productDetails;
}
