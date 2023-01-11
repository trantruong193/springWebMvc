package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.WishList;

import java.util.List;

public interface WishListService {
    void save(Long cusId,Long proId);
    void delete(Long id);
    List<WishList> getByUserId(Long userId);
}
