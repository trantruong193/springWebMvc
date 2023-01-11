package com.example.springWebMvc.persistent.dto;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReport {
    private int total;
    private int ordering;
    private int confirm;
    private int shipping;
    private int success;
    private int cancel;
    private int orderPer;
    private int confirmPer;
    private int shippingPer;
    private int successPer;
    private int cancelPer;
    private double totalPrice;
    public OrderReport(List<Order> orders){
        this.total = orders.size();
        this.totalPrice = orders.stream().mapToDouble(Order::getTotalPrice).sum();

        this.confirm = orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.Confirm))
                .toList().size();
        this.success = orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.Complete))
                .toList().size();
        this.shipping = orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.Shipping))
                .toList().size();
        this.cancel = orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.Cancel))
                .toList().size();


        this.ordering = this.total- this.confirm - this.cancel - this.shipping - this.success;

        this.successPer = this.success*100/this.total;
        this.cancelPer = this.cancel*100/this.total;
        this.shippingPer = this.shipping*100/this.total;
        this.confirmPer = this.confirm*100/this.total;
        this.orderPer = 100-this.successPer-this.cancelPer-this.confirmPer-this.shippingPer;
    }
}
