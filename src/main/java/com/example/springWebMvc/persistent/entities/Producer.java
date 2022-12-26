package com.example.springWebMvc.persistent.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producers")
public class Producer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producerId",nullable = false)
    private Long producerId;
    @Column(name = "producerName",nullable = false,unique = true)
    private String producerName;
    @Column(name = "logoUrl")
    private String imgUrl;
    @OneToMany(mappedBy = "producer")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products;
}
