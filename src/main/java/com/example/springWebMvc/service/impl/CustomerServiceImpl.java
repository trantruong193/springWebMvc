package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Customer;
import com.example.springWebMvc.repository.CustomerRepository;
import com.example.springWebMvc.repository.RoleRepository;
import com.example.springWebMvc.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean save(Customer customer) {
        try {
            customerRepository.save(customer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Customer findByUSerId(Long userId) {
        if (customerRepository.getCustomerByUser_UserId(userId) != null)
            return customerRepository.getCustomerByUser_UserId(userId);
        else
            return null;
    }

    @Override
    public boolean checkPhone(String phone) {
        return customerRepository.getCustomerByPhone(phone) != null;
    }

}
