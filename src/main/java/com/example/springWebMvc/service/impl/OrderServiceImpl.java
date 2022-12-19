package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Order;
import com.example.springWebMvc.repository.OrderRepository;
import com.example.springWebMvc.repository.TypeRepository;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            TypeRepository typeRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
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
}
