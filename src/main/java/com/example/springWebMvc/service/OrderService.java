package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface OrderService {
    List<Order> getAllByPhone(String phone);
    List<Order> getAllByUserId(Long userId);
    List<Order> getAll();
    List<Order> getAllByOrderId(Long orderId);
    Order getByOrderCode (String orderCode);
    Order save(Order order);
    Order getByOrderId(Long orderId);
    List<Order> findByPhoneStatusDate(String phone, OrderStatus status, Date start,Date end);
    List<Order> findByPhoneStatus(String phone, OrderStatus status);
    List<Order> findByPhoneDate(String phone, Date start,Date end);
    List<Order> findByStatusDate(OrderStatus status, Date start,Date end);
    List<Order> findByDate( Date start,Date end);
    List<Order> findByStatus(OrderStatus status);

}
