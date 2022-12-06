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
@Entity
@Table(name = "producers")
public class Producer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producerId",nullable = false)
    private Long producerId;
    @Column(name = "producerName",nullable = false,unique = true)
    private String producerName;

    @OneToMany(mappedBy = "producer")
    private List<Product> products;
}
