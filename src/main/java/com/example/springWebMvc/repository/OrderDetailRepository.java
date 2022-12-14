package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> getOrderDetailsByOrder_OrderId(Long orderId);
}
