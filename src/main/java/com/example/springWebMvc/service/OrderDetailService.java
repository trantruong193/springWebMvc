package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getAllByOrderId(Long orderId);
    boolean save(OrderDetail orderDetail);
}
