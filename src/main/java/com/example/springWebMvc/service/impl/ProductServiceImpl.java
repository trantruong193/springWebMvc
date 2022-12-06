package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.ProductStatus;
import com.example.springWebMvc.persistent.entities.Product;
import com.example.springWebMvc.repository.ProductRepository;
import com.example.springWebMvc.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public void update(Product product) {
        try {
            productRepository.save(product);
        }catch (Exception e){
            throw new RuntimeException("Can't update product" + e);
        }
    }
    @Override
    public boolean checkProduct(String name) {
        Product product =  productRepository.findProductsByProNameEqualsIgnoreCase(name);
        return product != null;
    }
    @Override
    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCatIdAndStatus(Long catId, ProductStatus status) {
        return productRepository.getProductsByCategory_CatIdAndStatus(catId,status);
    }

    @Override
    public List<Product> getProductByStatus(ProductStatus productStatus) {
        return productRepository.getProductsByStatus(productStatus);
    }

    @Override
    public Product findById(Long proId) {
        return productRepository.findById(proId).orElse(null);
    }

    @Override
    public Page<Product> findByName(String name,Pageable pageable) {
        return productRepository.findProductsByProNameContaining(name,pageable);
    }

    @Override
    public List<Product> findByCatId(Long catId) {
        return productRepository.findProductsByCategory_CatId(catId);
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByProNameContaining(name);
    }
    @Override
    public Page<Product> findByCategory(Long catId,Pageable pageable) {
        return productRepository.findProductsByCategory_CatId(catId,pageable);
    }

    @Override
    public Page<Product> findByProducer(Long producerId, Pageable pageable) {
        return productRepository.findProductsByProducer_ProducerId(producerId,pageable);
    }

    @Override
    public Page<Product> findByCategoryAndProducer(Long catId, Long producerId,Pageable pageable) {
        return productRepository.findProductsByCategory_CatIdAndProducer_ProducerId(catId,producerId,pageable);
    }

    @Override
    public Page<Product> findByNameAndCatId(String name, Long catId, Pageable pageable) {
        return productRepository.findProductsByProNameContainingAndCategory_CatId(name,catId,pageable);
    }

    @Override
    public Page<Product> findByNameAndProducerId(String name, Long producerId, Pageable pageable) {
        return productRepository.findProductsByProNameContainingAndProducer_ProducerId(name,producerId,pageable);
    }

    @Override
    public Page<Product> findByNameAndCatIdAndProducerId(String name, Long catId, Long producerId, Pageable pageable) {
        return productRepository.findProductsByProNameContainingAndCategory_CatIdAndProducer_ProducerId(name,catId,producerId,pageable);
    }
}
