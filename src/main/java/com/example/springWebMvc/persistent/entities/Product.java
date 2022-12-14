package com.example.springWebMvc.persistent.entities;

import com.example.springWebMvc.persistent.ProductStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proId",nullable = false)
    private Long proId;
    @Column(name = "proName",nullable = false)
    private String proName;
    @Column(name = "status",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductStatus status;
    @Column(name = "basePrice")
    private double basePrice;
    @Column(name = "discount")
    private double discount;
    @Column(name = "importDate",nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date importDate;
    @Column(name = "imgUrl")
    private String imgUrl;
    @Column(name = "imgUrl1")
    private String imgUrl1;
    @Column(name = "imgUrl2")
    private String imgUrl2;
    @Column(name = "imgUrl3")
    private String imgUrl3;
    @Column(name = "description",length = 1000)
    private String description;
    @Column(name = "description1",length = 1000)
    private String description1;
    @Column(name = "description2",length = 1000)
    private String description2;
    @Transient
    public int getRate(){
        if (reviews.size()>0){
            return reviews.stream().mapToInt(Review::getRate).sum()/reviews.size();
        }else
            return 0;
    }
    @Transient
    public double highestDiscount(){
        if (productDetails.size()>0){
            productDetails.sort(Comparator.comparing(ProductDetail::getDiscount));
            return productDetails.get(productDetails.size()-1).getDiscount();
        }
        return discount;
    }
    @ManyToOne
    @JoinColumn(name = "catId",referencedColumnName = "catId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "producerId",referencedColumnName = "producerId")
    private Producer producer;
    @ManyToOne
    @JoinColumn(name = "catalogId",referencedColumnName = "catalogId")
    private Catalog catalog;

    @OneToMany(mappedBy = "product")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ProductDetail> productDetails;

    @OneToMany(mappedBy = "product")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Review> reviews;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<WishList> wishLists;
}
