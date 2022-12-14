package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> getOrdersByPhone(String phone);
    List<Order> getOrdersByUserId(Long userId);
}
