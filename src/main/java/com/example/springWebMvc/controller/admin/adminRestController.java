package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.dto.CatalogDTO;
import com.example.springWebMvc.persistent.dto.ProductDTO;
import com.example.springWebMvc.persistent.dto.ProductDetailDTO;
import com.example.springWebMvc.persistent.entities.Catalog;
import com.example.springWebMvc.persistent.entities.Product;
import com.example.springWebMvc.persistent.entities.ProductDetail;
import com.example.springWebMvc.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class adminRestController {
    @Autowired
    CatalogService catalogService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductDetailService productDetailService;
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @GetMapping("/catalog")
    public List<CatalogDTO> getCatalog(@RequestParam("catId") Long catId){
        List<Catalog> catalogs = catalogService.getCatalogByCatId(catId);
        List<CatalogDTO> catalogDTOS = new ArrayList<>();
        for (Catalog catalog : catalogs){
            CatalogDTO catalogDTO = new CatalogDTO(catalog.getCatalogId(),catalog.getName());
            catalogDTOS.add(catalogDTO);
        }
        return catalogDTOS;
    }
    @GetMapping("/products")
    public List<ProductDTO> getProduct(@RequestParam("catId") Long catId){
        List<Product> products = productService.findByCatId(catId);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product p : products){
            ProductDTO productDTO = ProductDTO.builder()
                    .proId(p.getProId())
                    .proName(p.getProName())
                    .build();
            productDTOS.add(productDTO);
        }
        productDTOS.sort(Comparator.comparing(o->(o.getProName().toLowerCase())));
        return productDTOS;
    }
    @GetMapping("/productDetail")
    public ProductDetailDTO getProductDetail(@RequestParam("productDetailId") Long productDetailId){
        ProductDetail productDetail = productDetailService.findById(productDetailId);
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        BeanUtils.copyProperties(productDetail,productDetailDTO);
        return productDetailDTO;
    }
    @GetMapping("/getQuantity")
    public List<ProductDetailDTO> getQuantity(@RequestParam("proId") Long proId){
        List<ProductDetail> productDetails = productDetailService.getByProId(proId);
        List<ProductDetailDTO> productDetailDTOs = new ArrayList<>();
        productDetails.forEach(productDetail -> {
            ProductDetailDTO productDetailDTO;
            new ProductDetailDTO();
            productDetailDTO =  ProductDetailDTO.builder()
                    .colorName(productDetail.getColor().getColorName())
                    .typeName(productDetail.getType().getTypeName())
                    .quantity(productDetail.getQuantity()).build();
            productDetailDTOs.add(productDetailDTO);
        });
        productDetailDTOs.sort(Comparator.comparing(ProductDetailDTO::getTypeName));
        return productDetailDTOs;
    }
    @GetMapping("/addToCart")
    public int addToCart (@RequestParam(name = "productDetailId",required = false) Long cId,
                                            @RequestParam(name = "quantity",required = false) int quantity){
        cartService.add(cId,quantity);
        return cartService.getCount();
    }
    @GetMapping("/checkUsername")
    public boolean checkUsername(@RequestParam(name = "rUsername") String username){
        if (userService.getUserByUsername(username) != null){
            return true;
        }
        return false;
    }
}
