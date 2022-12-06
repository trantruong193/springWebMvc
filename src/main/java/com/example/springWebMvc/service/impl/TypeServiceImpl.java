package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Type;
import com.example.springWebMvc.repository.TypeRepository;
import com.example.springWebMvc.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;
    @Override
    public List<Type> getAll() {
        return typeRepository.findAll();
    }

    @Override
    public Type getById(Long typeId) {
        return typeRepository.findById(typeId).orElse(null);
    }
}
