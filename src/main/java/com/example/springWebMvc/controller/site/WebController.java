package com.example.springWebMvc.controller.site;

import com.example.springWebMvc.persistent.ProductStatus;
import com.example.springWebMvc.persistent.dto.CartItem;
import com.example.springWebMvc.persistent.dto.TypeDTO;
import com.example.springWebMvc.persistent.entities.*;
import com.example.springWebMvc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/site")
// http://localhost:8080/site
public class WebController {
    ProductService productService;
    ProducerService producerService;
    CategoryService categoryService;
    CatalogService catalogService;
    ProductDetailService productDetailService;
    CartService cartService;
    UserService userService;
    RoleService roleService;
    PasswordEncoder passwordEncoder;
    @Autowired
    public WebController (ProductService productService,
                          CategoryService categoryService,
                          ProducerService producerService,
                          CatalogService catalogService,
                          ProductDetailService productDetailService,
                          CartService cartService,
                          UserService userService,
                          RoleService roleService,
                          PasswordEncoder passwordEncoder){
        this.categoryService = categoryService;
        this.catalogService = catalogService;
        this.productService = productService;
        this.producerService = producerService;
        this.productDetailService = productDetailService;
        this.cartService = cartService;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }
    @ModelAttribute("feature")
    public List<Product> getFeatureProduct(){
        return productService.getProductByStatus(ProductStatus.Feature);
    }
    @ModelAttribute("categories")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }
    @ModelAttribute("producers")
    public List<Producer> getAllProducers(){
        return producerService.getAll();
    }
    @ModelAttribute("catalogs")
    public List<Catalog> getAllCatalogs(){
        return catalogService.getAll();
    }
    @ModelAttribute("cartItems")
    public Collection<CartItem> getCartItems(){
        return cartService.getCartItems();
    }
    @ModelAttribute("cartQuantity")
    public int getCartQuantity(){
        return cartService.getCount();
    }
    @ModelAttribute("amount")
    public double getAmount(){
        return cartService.getAmount();
    }
    @GetMapping("")
    private String home(Model model){
        model.addAttribute("phones",productService.getProductByCatIdAndStatus(1L, ProductStatus.Feature));
        model.addAttribute("laptops",productService.getProductByCatIdAndStatus(2L, ProductStatus.Feature));
        model.addAttribute("tablets",productService.getProductByCatIdAndStatus(3L, ProductStatus.Feature));
        model.addAttribute("watches",productService.getProductByCatIdAndStatus(4L, ProductStatus.Feature));
        model.addAttribute("accessories",productService.getProductByCatIdAndStatus(5L, ProductStatus.Feature));
        model.addAttribute("ears",productService.getProductByCatIdAndStatus(6L, ProductStatus.Feature));
        return "site/fragment/home";
    }
    @GetMapping("cart")
    private String cart(Model model){

        return "site/fragment/cart";
    }
    @GetMapping("updateCart")
    private String updateCart(@RequestParam(name = "productDetailId",required = false)Long productDetailId,
                              @RequestParam(name = "quantity") int quantity){
        cartService.update(productDetailId,quantity);
        return "redirect:/site/cart";
    }
    @GetMapping("removeCart/{productDetailId}")
    private String removeCart(@PathVariable("productDetailId")Long productDetailId){
        cartService.remove(productDetailId);
        return "redirect:/site/cart";
    }
    @GetMapping("checkout")
    private String checkout(){
        return "site/fragment/checkout";
    }
    @GetMapping("contact")
    private String contact(){
        return "site/fragment/contact";
    }
    @PostMapping("register")
    private String register(Model model,
                            @RequestParam("rUsername") String username,
                            @RequestParam("rPassword") String password){
            if (username.length() < 3 || username.length() > 20)
            {
                model.addAttribute("uMessage","Tên đăng nhập phải từ 3 tới 20 kí tự");
                return "/site/fragment/login";
            }
            if (password.length() < 6 || password.length() > 20)
            {
                model.addAttribute("pMessage","Mật khẩu phải từ 6 tới 20 kí tự");
                return "/site/fragment/login";
            }
            if (userService.getUserByUsername(username) != null){
                model.addAttribute("message","Tên đăng nhập đã tồn tại");
                return "/site/fragment/login";
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setStatus(1);
            userService.save(user);
            Role role = new Role();
            role.setUser(userService.getUserByUsername(username));
            role.setAuthority(Authority.builder().authorityId(3L).authorityName("ROLE_CUSTOMER").build());
            roleService.saveRole(role);
        return "site/fragment/home";
    }
    @GetMapping("detail/{proId}")
    private String detail( Model model,
                           @PathVariable("proId") Long proId,
                           @RequestParam(name = "typeId", required = false)Long typeId){
        if (typeId != null){
            model.addAttribute("productDetails",productDetailService.getByProIdAndTypeId(proId,typeId));
        }
        model.addAttribute("product",productService.findById(proId));
        List<ProductDetail> productDetails = productDetailService.getByProId(proId);
        List<TypeDTO> types = new ArrayList<>();
        productDetails.forEach(productDetail -> {
            TypeDTO type = new TypeDTO();
            type.setTypeId(productDetail.getType().getTypeId());
            type.setTypeName(productDetail.getType().getTypeName());
            types.add(type);
        });
        HashSet<TypeDTO> typeDTOHashSet = new HashSet<>(types);
        model.addAttribute("types",typeDTOHashSet);
        model.addAttribute("typeId",typeId);
        return "site/fragment/productDetail";
    }
    @GetMapping("products")
    private String shop(Model model,
                        @RequestParam(name = "producerId",required = false) Long producerId,
                        @RequestParam(name = "catId",required = false) Long catId,
                        @RequestParam(name = "search",required = false) String search,
                        @PageableDefault(size = 9,sort = "proName",direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> pages;
        if (StringUtils.hasText(search)){
            if (producerId == null && catId == null){
                pages = productService.findByName(search,pageable);
            }else if(producerId != null && catId == null){
                pages = productService.findByNameAndProducerId(search,producerId,pageable);
            }else if(producerId == null){
                pages = productService.findByNameAndCatId(search,catId,pageable);
            }else{
                pages = productService.findByNameAndCatIdAndProducerId(search,catId,producerId,pageable);
            }
        }else {
            if (producerId == null){
                if (catId == null)
                    pages = productService.getAll(pageable);
                else
                    pages = productService.findByCategory(catId,pageable);
            }else {
                if (catId == null)
                    pages = productService.findByProducer(producerId,pageable);
                else
                    pages = productService.findByCategoryAndProducer(catId,producerId,pageable);
            }
        }
        model.addAttribute("pages",pages);
        model.addAttribute("search",search);
        model.addAttribute("sort","proName,asc");
        model.addAttribute("producerId",producerId);
        model.addAttribute("catId",catId);
        return "site/fragment/allProducts";
    }
}
