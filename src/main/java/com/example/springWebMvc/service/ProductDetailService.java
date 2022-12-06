package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.ProductDetail;

import java.util.List;
import java.util.Objects;

public interface ProductDetailService {
    List<ProductDetail> getAll();
    List<ProductDetail> getByProId(Long proId);
    List<ProductDetail> getByProIdAndTypeId(Long proId,Long typeId);
    ProductDetail findByProIdAndColorIdAndTypeId(Long proId,Long colorId,Long typeId);
    boolean save(ProductDetail productDetail);
    ProductDetail findById(Long productDetailId);
}
