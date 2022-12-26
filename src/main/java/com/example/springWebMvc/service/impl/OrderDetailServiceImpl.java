package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.OrderDetail;
import com.example.springWebMvc.repository.OrderDetailRepository;
import com.example.springWebMvc.repository.OrderRepository;
import com.example.springWebMvc.repository.TypeRepository;
import com.example.springWebMvc.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private OrderDetailRepository repository;
    private final TypeRepository typeRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository repository,
                                  TypeRepository typeRepository,
                                  OrderRepository orderRepository) {
        this.repository = repository;
        this.typeRepository = typeRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDetail> getAllByOrderId(Long orderId) {
        return repository.getOrderDetailsByOrder_OrderId(orderId);
    }

    @Override
    public boolean save(OrderDetail orderDetail) {
        try {
            repository.save(orderDetail);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public OrderDetail getById(Long orderDetailId) {
        return repository.getReferenceById(orderDetailId);
    }

    @Override
    public void delete(Long detailId) {
        try {
            repository.deleteById(detailId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

