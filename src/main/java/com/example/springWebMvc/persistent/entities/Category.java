package com.example.springWebMvc.persistent.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catId",nullable = false)
    private Long catId;
    @Column(name = "catName",nullable = false,unique = true)
    private String catName;
    @Column(name = "status")
    private Boolean status = true;

    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products;
    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Catalog> catalogs;
}
