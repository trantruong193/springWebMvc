package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Customer;

import java.util.Optional;

public interface CustomerService {
    boolean save(Customer customer);
    Customer findByUSerId(Long userId);
    boolean checkPhone(String phone);
}
