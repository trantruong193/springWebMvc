package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Type;

import java.util.List;

public interface TypeService {
    List<Type> getAll();
    Type getById(Long typeId);
}
