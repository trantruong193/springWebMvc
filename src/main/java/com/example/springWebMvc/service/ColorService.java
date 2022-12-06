package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Color;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    List<Color> getAll();
    Color getColorById(Long colorId);
}
