package com.example.springWebMvc.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogDTO implements Serializable {
    private Long catalogId;
    private String name;
}
