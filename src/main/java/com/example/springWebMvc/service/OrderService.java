package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllByPhone(String phone);
    List<Order> getAllByUserId(Long userId);
    boolean save(Order order);
}
