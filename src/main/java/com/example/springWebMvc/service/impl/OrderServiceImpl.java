package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.entities.Order;
import com.example.springWebMvc.repository.OrderRepository;
import com.example.springWebMvc.repository.TypeRepository;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    UserRepository userRepository;
    TypeRepository typeRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            TypeRepository typeRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
    }

    @Override
    public Order getByOrderId(Long orderId) {
        return orderRepository.getReferenceById(orderId);
    }

    @Override
    public List<Order> getAllByPhone(String phone) {
        return orderRepository.getOrdersByPhone(phone);
    }

    @Override
    public List<Order> getAllByUserId(Long userId) {
        return orderRepository.getOrdersByUserId(userId);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getByOrderCode(String orderCode) {
        return orderRepository.getOrderByOrderCode(orderCode);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllByOrderId(Long orderId) {
        return orderRepository.getOrdersByOrderId(orderId);
    }

    @Override
    public List<Order> findByPhoneStatusDate(String phone, OrderStatus status, Date start, Date end) {
        return orderRepository.getOrdersByPhoneAndStatusAndCreateTimeBetween(phone,status,start,end);
    }

    @Override
    public List<Order> findByPhoneStatus(String phone, OrderStatus status) {
        return orderRepository.getOrdersByPhoneAndStatus(phone,status);
    }

    @Override
    public List<Order> findByPhoneDate(String phone, Date start, Date end) {
        return orderRepository.getOrdersByPhoneAndCreateTimeBetween(phone,start,end);
    }

    @Override
    public List<Order> findByStatusDate(OrderStatus status, Date start, Date end) {
        return orderRepository.getOrdersByStatusAndCreateTimeBetween(status,start,end);
    }

    @Override
    public List<Order> findByDate(Date start, Date end) {
        return orderRepository.getOrdersByCreateTimeBetween(start,end);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.getOrdersByStatus(status);
    }

}
