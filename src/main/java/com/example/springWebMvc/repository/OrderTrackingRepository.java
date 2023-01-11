package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.OrderTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTrackingRepository extends JpaRepository<OrderTracking,Long> {
    List<OrderTracking> getOrderTrackingByOrder_OrderId(Long orderId);
    void deleteOrderTrackingsByOrder_OrderId(Long orderId);
}
