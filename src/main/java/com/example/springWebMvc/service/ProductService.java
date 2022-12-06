package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.ProductStatus;
import com.example.springWebMvc.persistent.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    void update (Product product);
    boolean checkProduct(String name);
    List<Product> getAll();
    List<Product> getProductByCatIdAndStatus(Long catId, ProductStatus status);
    Page<Product> getAll(Pageable pageable);
    Product findById(Long proId);
    Page<Product> findByName(String name,Pageable pageable);
    Page<Product> findByNameAndCatId(String name,Long catId,Pageable pageable);
    Page<Product> findByNameAndProducerId(String name,Long producerId,Pageable pageable);
    Page<Product> findByNameAndCatIdAndProducerId(String name,Long catId,Long producerId,Pageable pageable);
    List<Product> findByName(String name);
    List<Product> findByCatId(Long catId);
    Page<Product> findByCategory(Long catId,Pageable pageable);
    Page<Product> findByProducer(Long catId,Pageable pageable);
    List<Product> getProductByStatus(ProductStatus productStatus);
    Page<Product> findByCategoryAndProducer(Long catId,Long producerId,Pageable pageable);
}

