package com.example.springWebMvc.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "catalogs")
@Entity
public class Catalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalogId")
    private Long catalogId;
    @Column(name = "catalog")
    private String name;

    @ManyToOne
    @JoinColumn(name = "catId",referencedColumnName = "catId")
    private Category category;

    @OneToMany(mappedBy = "catalog")
    private List<Product> products;
}
