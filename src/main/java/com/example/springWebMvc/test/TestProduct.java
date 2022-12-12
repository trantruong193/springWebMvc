package com.example.springWebMvc.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class TestProduct {
    @Id
    private Long id;
    private String name;
    private String type1Name;
    private String type2Name;
}
