package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.entities.Order;
import com.example.springWebMvc.persistent.entities.OrderTracking;
import com.example.springWebMvc.repository.OrderRepository;
import com.example.springWebMvc.repository.OrderTrackingRepository;
import com.example.springWebMvc.service.OrderTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderTrackingServiceImpl implements OrderTrackingService {

    OrderTrackingRepository repository;
    OrderRepository orderRepository;
    @Autowired
    public OrderTrackingServiceImpl(OrderTrackingRepository repository,
                                    OrderRepository orderRepository){
        this.repository = repository;
        this.orderRepository = orderRepository;
    }
    @Override
    public OrderTracking save(OrderTracking orderTracking) {
        return repository.save(orderTracking);
    }

    @Override
    public List<OrderTracking> getAllByOrderId(Long orderId) {
        return repository.getOrderTrackingByOrder_OrderId(orderId);
    }

    @Override
    public void trackOrder(OrderStatus status, Long orderId,String message) {
        Order order  = orderRepository.findById(orderId).orElse(null);
        if (order != null){
            OrderTracking tracking = new OrderTracking();
            tracking.setOrder(order);
            tracking.setOrderStatus(status);
            tracking.setDescription(message);
            repository.save(tracking);
        }
    }

    @Override
    @Transactional
    public void deleteTrackingOrder(Long orderId) {
        repository.deleteOrderTrackingsByOrder_OrderId(orderId);
    }
}
