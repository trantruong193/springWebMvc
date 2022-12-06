package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.dto.ProductDetailDTO;
import com.example.springWebMvc.persistent.entities.Category;
import com.example.springWebMvc.persistent.entities.Color;
import com.example.springWebMvc.persistent.entities.ProductDetail;
import com.example.springWebMvc.persistent.entities.Type;
import com.example.springWebMvc.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("admin/repository")
// http://localhost:8080/admin/repository/add
public class RepositController {

    private final ColorService colorService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductDetailService productDetailService;
    private final TypeService typeService;
    @Autowired
    public RepositController (ColorService colorService,
                              ProductService productService,
                              CategoryService categoryService,
                              ProductDetailService productDetailService,
                              TypeService typeService){
        this.colorService = colorService;
        this.productService = productService;
        this.productDetailService = productDetailService;
        this.categoryService = categoryService;
        this.typeService = typeService;
    }
    @ModelAttribute("colors")
    public List<Color> getColors(){
        return colorService.getAll();
    }
    @ModelAttribute("categories")
    public List<Category> getProducts(){
        return categoryService.getAllCategories();
    }
    @ModelAttribute("types")
    public List<Type> getTypes(){
        return typeService.getAll();
    }
    @RequestMapping("")
    public String repository(Model model,
                             @RequestParam(name = "proId",required = false) Long proId){
        List<ProductDetail> list;
        if(proId != null){
            list = productDetailService.getByProId(proId);
            list.sort(Comparator.comparing(o -> (o.getType().getTypeId())));
        }else {
            list = productDetailService.getAll();
            list.sort(Comparator.comparing(o -> (o.getProduct().getProId())));
        }
        list.sort(Comparator.comparing(o -> (o.getProduct().getProId())));
        model.addAttribute("list",list);
        return "/admin/fragment/repository/list";
    }
    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("pD",new ProductDetailDTO());
        return "/admin/fragment/repository/add";
    }
    @PostMapping("save")
    public String save(Model model,
                       @Valid @ModelAttribute("pD") ProductDetailDTO detailDTO,
                       BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "/admin/fragment/repository/add";
        ProductDetail productDetail = new ProductDetail();
        if (productDetailService.findByProIdAndColorIdAndTypeId(detailDTO.getProId(), detailDTO.getColorId(), detailDTO.getTypeId()) == null){
            BeanUtils.copyProperties(detailDTO,productDetail);
            productDetail.setProduct(productService.findById(detailDTO.getProId()));
            productDetail.setColor(colorService.getColorById(detailDTO.getColorId()));
            productDetail.setType(typeService.getById(detailDTO.getTypeId()));
        }else {
            productDetail = productDetailService.findByProIdAndColorIdAndTypeId(detailDTO.getProId(), detailDTO.getColorId(), detailDTO.getTypeId());
            productDetail.setDiscount(detailDTO.getDiscount());
            productDetail.setQuantity(detailDTO.getQuantity());
        }
        productDetailService.save(productDetail);
        model.addAttribute("message","Add successfully");
        return "forward:/admin/repository";
    }
    @PostMapping("update/{productDetailId}")
    public String update(Model model,
                         @PathVariable("productDetailId") Long productDetailId,
                         @RequestParam("discount") double discount,
                         @RequestParam("quantity") int quantity){
        if (discount < 0 || discount > 100 || quantity < 0){
            model.addAttribute("message","Invalid data");
            return "admin/fragment/repository/list";
        }
        ProductDetail productDetail = productDetailService.findById(productDetailId);
        if (productDetail != null){
            productDetail.setDiscount(discount);
            productDetail.setQuantity(quantity);
            productDetailService.save(productDetail);
        }
        model.addAttribute("message","Update successfully");
        return "forward:/admin/repository";
    }
}
