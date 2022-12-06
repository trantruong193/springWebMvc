package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.dto.CartItem;

import java.util.Collection;

public interface CartService {
    void add(Long productDetailId,int quantity);

    void remove(Long productDetailId);

    Collection<CartItem> getCartItems();

    void clear();

    void update(Long productDetailId, int quantity);

    double getAmount();

    int getCount();
}
