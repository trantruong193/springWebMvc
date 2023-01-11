package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.dto.ProductDTO;
import com.example.springWebMvc.persistent.dto.ProductDetailDTO;
import com.example.springWebMvc.persistent.entities.*;
import com.example.springWebMvc.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @ModelAttribute("link")
    public String getLink(){
        return "repository";
    }
    @RequestMapping("")
    public String repository(Model model,
                             @RequestParam(name = "proId",required = false) Long proId){
        List<Product> productList = new ArrayList<>();
        List<ProductDTO> list = new ArrayList<>();
        if(proId != null){
            if (productService.findById(proId) != null){
                productList.add(productService.findById(proId));
                productList.forEach(product -> {
                    list.add(new ProductDTO(product));
                });
            }
        }else {
            productList = productService.getAll();
            if (!productList.isEmpty()){
                productList.forEach(product -> {
                    list.add(new ProductDTO(product));
                });
            }
        }
        list.sort(Comparator.comparing(p->(p.getProName().toLowerCase())));
        model.addAttribute("list",list);
        model.addAttribute("pD",new ProductDetailDTO());
        return "/admin/fragment/repository/list";
    }
    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("pD",new ProductDetailDTO());
        return "/admin/fragment/repository/add";
    }
    @PostMapping("save")
    public String save(Model model,@RequestParam(name = "mode",required = false) String mode,
                       @RequestParam(name = "proId",required = false) Long proId,
                       @Valid @ModelAttribute("pD") ProductDetailDTO detailDTO,
                       BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            if (mode!=null){
                if (mode.equals("listMode")){
                    model.addAttribute("errorMessage","Insert fail. Invalid data");
                    return "forward:/admin/repository";
                }
            }
            model.addAttribute("errorMessage","Insert fail. Invalid data");
            return "/admin/fragment/repository/add";
        }
        if (detailDTO.getProId() == null && mode == null){
            model.addAttribute("errorMessage","Insert fail. Invalid data");
            return "/admin/fragment/repository/add";
        }
        ProductDetail productDetail = new ProductDetail();
        // case field proId has exits (update product detail)
        if (detailDTO.getProId() != null){
            if (productDetailService.findByProIdAndColorIdAndTypeId(detailDTO.getProId(), detailDTO.getColorId(), detailDTO.getTypeId()) == null){
                // if not found creat new product detail
                BeanUtils.copyProperties(detailDTO,productDetail);
                productDetail.setProduct(productService.findById(detailDTO.getProId()));
                productDetail.setColor(colorService.getColorById(detailDTO.getColorId()));
                productDetail.setType(typeService.getById(detailDTO.getTypeId()));
            }else {
                // if found set new data
                productDetail = productDetailService.findByProIdAndColorIdAndTypeId(detailDTO.getProId(), detailDTO.getColorId(), detailDTO.getTypeId());
                productDetail.setDiscount(detailDTO.getDiscount());
                productDetail.setQuantity(detailDTO.getQuantity());
            }
        }else {
            // case field proId doesn't exist (create new)
            if (productDetailService.findByProIdAndColorIdAndTypeId(proId, detailDTO.getColorId(), detailDTO.getTypeId()) == null){
                BeanUtils.copyProperties(detailDTO,productDetail);
                productDetail.setProduct(productService.findById(detailDTO.getProId()));
                productDetail.setColor(colorService.getColorById(detailDTO.getColorId()));
                productDetail.setType(typeService.getById(detailDTO.getTypeId()));
            }else {
                productDetail = productDetailService.findByProIdAndColorIdAndTypeId(proId, detailDTO.getColorId(), detailDTO.getTypeId());
                productDetail.setDiscount(detailDTO.getDiscount());
                productDetail.setQuantity(detailDTO.getQuantity());
            }
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
        if (discount > 100 || quantity < 0){
            model.addAttribute("errorMessage","Invalid data");
            return "forward:/admin/repository";
        }
        ProductDetail productDetail = productDetailService.findById(productDetailId);
        if (productDetail != null){
            productDetail.setDiscount(discount);
            productDetail.setQuantity(quantity);
            productDetailService.save(productDetail);
            model.addAttribute("message","Update successfully");
        }else {
            model.addAttribute("errorMessage","Invalid data");
        }
        return "forward:/admin/repository";
    }
}
