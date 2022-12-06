package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Producer;
import com.example.springWebMvc.repository.ProducerRepository;
import com.example.springWebMvc.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerServiceImpl implements ProducerService {
    private ProducerRepository producerRepository;
    @Autowired
    public ProducerServiceImpl (ProducerRepository producerRepository){
        this.producerRepository = producerRepository;
    }
    @Override
    public List<Producer> getAll() {
        return producerRepository.findAll();
    }

    @Override
    public Producer findById(Long producerId) {
        return producerRepository.findById(producerId).orElse(null);
    }
}
