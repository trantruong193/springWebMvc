package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.entities.OrderTracking;

import java.util.List;

public interface OrderTrackingService {
    OrderTracking save(OrderTracking orderTracking);
    List <OrderTracking> getAllByOrderId(Long orderId);
    void deleteTrackingOrder(Long orderId);
    void trackOrder(OrderStatus status,Long orderId,String message);
}
