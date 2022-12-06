package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.ProductStatus;
import com.example.springWebMvc.persistent.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findProductsByProNameContaining(String name, Pageable pageable);
    Page<Product> findProductsByProNameContainingAndCategory_CatId(String name,Long catId, Pageable pageable);
    Page<Product> findProductsByProNameContainingAndProducer_ProducerId(String name,Long producerId, Pageable pageable);
    Page<Product> findProductsByProNameContainingAndCategory_CatIdAndProducer_ProducerId(String name,Long catId,Long producerId, Pageable pageable);
    List<Product> findProductsByCategory_CatId(Long catId);
    Page<Product> findProductsByCategory_CatId(Long catId,Pageable pageable);
    Product findProductsByProNameEqualsIgnoreCase(String name);
//    @Query("select p from Product p where p.category.catId = ?1 and p.status = ?2")
    List<Product> getProductsByCategory_CatIdAndStatus(Long catId, ProductStatus status);
    List<Product> getProductsByStatus(ProductStatus productStatus);
    List<Product> findByProNameContaining(String search);
    Page<Product> findProductsByCategory_CatIdAndProducer_ProducerId(Long catId,Long producerId,Pageable pageable);
    Page<Product> findProductsByProducer_ProducerId(Long producerId,Pageable pageable);
}
