package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.dto.CartItem;
import com.example.springWebMvc.persistent.entities.ProductDetail;
import com.example.springWebMvc.service.CartService;
import com.example.springWebMvc.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CartServiceImpl implements CartService {
    ProductDetailService productDetailService;
    @Autowired
    public CartServiceImpl(ProductDetailService productDetailService){
        this.productDetailService = productDetailService;
    }
    private final Map<Long,CartItem> map = new HashMap<>();
    @Override
    public void add(Long productDetailId,int quantity) {

        CartItem cartItem = map.get(productDetailId);
        if (cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }else {
            cartItem = new CartItem();
            ProductDetail productDetail = productDetailService.findById(productDetailId);
            cartItem.setProductDetailId(productDetailId);
            cartItem.setImgUrl(productDetail.getProduct().getImgUrl());
            cartItem.setProductDetailName(productDetail.getProduct().getProName());
            cartItem.setTypeName(productDetail.getType().getTypeName());
            cartItem.setColorName(productDetail.getColor().getColorName());
            cartItem.setPrice(productDetail.getProduct().getBasePrice()
                    -productDetail.getProduct().getBasePrice()*productDetail.getDiscount()/100);
            cartItem.setQuantity(quantity);
        }
        map.put(productDetailId,cartItem);
    }

    @Override
    public void remove(Long productDetailId) {
        map.remove(productDetailId);
    }

    @Override
    public Collection<CartItem> getCartItems() {
        return map.values();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void update(Long productDetailId, int quantity) {
        CartItem cartItem = map.get(productDetailId);
        cartItem.setQuantity(quantity);
        if (cartItem.getQuantity() <=0)
            map.remove(productDetailId);
    }

    @Override
    public double getAmount() {
        return map.values().stream().mapToDouble(item-> item.getQuantity()* item.getPrice()).sum();
    }

    @Override
    public int getCount() {
        if (map.isEmpty()){
            return 0;
        }
        return map.values().stream().mapToInt(CartItem::getQuantity).sum();
    }
}
