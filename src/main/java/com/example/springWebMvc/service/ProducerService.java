package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Producer;

import java.util.List;

public interface ProducerService {
    List<Producer> getAll();
    Producer findById(Long producerId);

}
