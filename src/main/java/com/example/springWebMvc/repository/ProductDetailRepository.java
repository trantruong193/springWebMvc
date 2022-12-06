package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long>{
    List<ProductDetail> getProductDetailByProduct_ProId(Long proId);
    List<ProductDetail> getProductDetailByProduct_ProIdAndType_TypeId(Long proId,Long typeId);
    ProductDetail findProductDetailByProduct_ProIdAndColor_ColorIdAndType_TypeId(Long proId,Long colorId,Long typeId);
}
