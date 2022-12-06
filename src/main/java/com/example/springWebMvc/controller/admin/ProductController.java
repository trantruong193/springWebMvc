package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.dto.ProductDTO;
import com.example.springWebMvc.persistent.entities.Category;
import com.example.springWebMvc.persistent.entities.Producer;
import com.example.springWebMvc.persistent.entities.Product;
import com.example.springWebMvc.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("admin/products")
// http://localhost:8080/admin/products
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProducerService producerService;
    private final FileUploadService fileUploadService;
    private final CatalogService catalogService;
    private final List<String> list = Arrays.asList("proId,asc","proName,asc","status,asc","basePrice,asc","discount,asc","category,asc","producer,asc","importDate,asc",
                                                    "proId,desc","proName,desc","status,desc","basePrice,desc","discount,desc","category,desc","producer,desc","importDate,desc");
    @Autowired
    public ProductController(CategoryService categoryService,ProductService productService,CatalogService catalogService,
                             ProducerService producerService,FileUploadService fileUploadService){
        this.categoryService = categoryService;
        this.productService = productService;
        this.producerService = producerService;
        this.catalogService = catalogService;
        this.fileUploadService = fileUploadService;
    }

    @ModelAttribute("categories")
    public List<Category> getCategories(){
        return categoryService.getAllCategories();
    }
    @ModelAttribute("producers")
    public List<Producer> getProducers(){
        return producerService.getAll();
    }

    @RequestMapping("")
    public String list(Model model,
                       @RequestParam(name = "sort",required = false) String sortType,
                       @RequestParam(value = "search", required = false) String search,
                       @PageableDefault(size = 10,sort = "proId",direction = Sort.Direction.ASC)Pageable pageable){
        Page<Product> pages;
        if (!list.contains(sortType)){
            pageable = PageRequest.of(0,10, Sort.Direction.ASC,"proId");
        }
        if (StringUtils.hasText(search)){
            pages = productService.findByName(search,pageable);
        }else {
            pages = productService.getAll(pageable);
        }
        List<Sort.Order> orderList = pages.getSort().stream().toList();
        if (orderList.size()>0){
            Sort.Order order = orderList.get(0);
            model.addAttribute("sort",
                    (order.getProperty() + "," + (order.getDirection() == Sort.Direction.ASC?"asc":"desc")));
        }else {
            model.addAttribute("sort","proId,asc");
        }
        model.addAttribute("pages",pages);
        model.addAttribute("search",search);
        return "admin/fragment/products/list";
    }
    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("product",new ProductDTO());
        return "admin/fragment/products/add";
    }
    @PostMapping("save")
    public String save(ModelMap model, @RequestParam("imgFile") MultipartFile[] multipartFile,
                             @Valid @ModelAttribute("product") ProductDTO productDTO,
                             BindingResult bindingResult) throws IOException {
        // Check form input and return if form has error
        if (bindingResult.hasErrors()) {
            return "admin/fragment/products/add";
        }
        if (productDTO.getProId() == null){
          if(productService.checkProduct(productDTO.getProName())){
              model.addAttribute("message","Tên sản phẩm đã trùng");
              return "admin/fragment/products/add";
          }
        }
        // Copy properties
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        // Delete old image
        if(productDTO.getProId() != null){
            Product p = productService.findById(productDTO.getProId());
            if (p.getImgUrl()!= null)
                fileUploadService.delete(p.getImgUrl());
            if (p.getImgUrl1()!= null)
                fileUploadService.delete(p.getImgUrl1());
            if (p.getImgUrl2()!= null)
                fileUploadService.delete(p.getImgUrl2());
            if (p.getImgUrl3()!= null)
                fileUploadService.delete(p.getImgUrl3());
        }
        // Save image file
        for(int i = 0; i < multipartFile.length; i++) {
            if (i==0)
                if (!multipartFile[i].isEmpty())
                    product.setImgUrl(fileUploadService.save(multipartFile[i]));
            if (i==1)
                if (!multipartFile[i].isEmpty())
                    product.setImgUrl1(fileUploadService.save(multipartFile[i]));
            if (i==2)
                if (!multipartFile[i].isEmpty())
                    product.setImgUrl2(fileUploadService.save(multipartFile[i]));
            if (i==3)
                if (!multipartFile[i].isEmpty())
                    product.setImgUrl3(fileUploadService.save(multipartFile[i]));
        }
        // Set new properties for product
        product.setCategory(categoryService.findById(productDTO.getCatId()));
        product.setProducer(producerService.findById(productDTO.getProducerId()));
        product.setCatalog(catalogService.getCatalogById(productDTO.getCatalogId()));
        productService.update(product);
        model.addAttribute("message", "Cập nhật thành công");
        return "forward:/admin/products";
    }
    @GetMapping("update/{proId}")
    public String edit(Model model,@PathVariable("proId") Long proId){
        Product product = productService.findById(proId);
        ProductDTO productDTO = new ProductDTO();
        if (product != null){
            BeanUtils.copyProperties(product,productDTO);
            model.addAttribute("product",productDTO);
        }else {
            model.addAttribute("message","Không tìm thấy sản phẩm");
            model.addAttribute("product",productDTO);
        }
        return "admin/fragment/products/add";
    }
    @GetMapping("detail/{proId}")
    public String detail(Model model,@PathVariable("proId") Long proId){
        Product product = productService.findById(proId);
        if (product != null){
            model.addAttribute("product",product);
        }else {
            model.addAttribute("message","Không tìm thấy sản phẩm");
        }
        return "admin/fragment/products/detail";
    }

}
