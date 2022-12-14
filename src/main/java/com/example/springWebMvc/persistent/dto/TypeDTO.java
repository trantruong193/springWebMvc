package com.example.springWebMvc.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeDTO implements Serializable {
    private Long typeId;
    private String typeName;
}
