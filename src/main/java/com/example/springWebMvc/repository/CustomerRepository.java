package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer getCustomerByUser_UserId(Long userId);
}
