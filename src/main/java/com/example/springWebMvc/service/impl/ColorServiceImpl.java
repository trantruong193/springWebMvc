package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Color;
import com.example.springWebMvc.repository.ColorRepository;
import com.example.springWebMvc.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ColorServiceImpl implements ColorService {
    private ColorRepository colorRepository;
    @Autowired
    public ColorServiceImpl(ColorRepository colorRepository){
        this.colorRepository = colorRepository;
    }
    @Override
    public List<Color> getAll() {
        return colorRepository.findAll();
    }

    @Override
    public Color getColorById(Long colorId) {
        return colorRepository.findById(colorId).orElse(null);
    }
}
