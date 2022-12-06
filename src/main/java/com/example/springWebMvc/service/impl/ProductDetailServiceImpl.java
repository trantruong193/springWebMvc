package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.ProductDetail;
import com.example.springWebMvc.repository.ProductDetailRepository;
import com.example.springWebMvc.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    private ProductDetailRepository productDetailRepository;
    @Autowired
    public ProductDetailServiceImpl (ProductDetailRepository productDetailRepository){
        this.productDetailRepository = productDetailRepository;
    }
    @Override
    public List<ProductDetail> getAll() {
        return productDetailRepository.findAll();
    }

    @Override
    public ProductDetail findById(Long productDetailId) {
        return productDetailRepository.findById(productDetailId).orElse(null);
    }

    @Override
    public List<ProductDetail> getByProId(Long proId) {
        return productDetailRepository.getProductDetailByProduct_ProId(proId);
    }

    @Override
    public List<ProductDetail> getByProIdAndTypeId(Long proId,Long typeId) {
        return productDetailRepository.getProductDetailByProduct_ProIdAndType_TypeId(proId,typeId);
    }

    @Override
    public ProductDetail findByProIdAndColorIdAndTypeId(Long proId, Long colorId,Long typeId) {
        return productDetailRepository.findProductDetailByProduct_ProIdAndColor_ColorIdAndType_TypeId(proId,colorId,typeId);
    }


    @Override
    public boolean save(ProductDetail productDetail) {
        try {
            productDetailRepository.save(productDetail);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
