package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> getOrdersByPhone(String phone);
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getOrdersByOrderId(Long orderId);
    Order getOrderByOrderCode (String orderCode);
    List<Order> getOrdersByPhoneAndStatusAndCreateTimeBetween(String phone, OrderStatus status, Date start,Date end);
    List<Order> getOrdersByPhoneAndStatus(String phone, OrderStatus status);
    List<Order> getOrdersByPhoneAndCreateTimeBetween(String phone, Date start,Date end);
    List<Order> getOrdersByStatusAndCreateTimeBetween(OrderStatus status, Date start,Date end);
    List<Order> getOrdersByCreateTimeBetween(Date start,Date end);
    List<Order> getOrdersByStatus(OrderStatus status);
}
