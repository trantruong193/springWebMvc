package com.example.springWebMvc.controller.site;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.PaymentMethod;
import com.example.springWebMvc.persistent.ProductStatus;
import com.example.springWebMvc.persistent.dto.*;
import com.example.springWebMvc.persistent.entities.*;
import com.example.springWebMvc.repository.CustomerRepository;
import com.example.springWebMvc.repository.OrderRepository;
import com.example.springWebMvc.repository.RoleRepository;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.service.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = {"/site"})
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
    CustomerService customerService;
    FileUploadService fileUploadService;
    PasswordEncoder passwordEncoder;
    BannerService bannerService;
    OrderDetailService orderDetailService;
    OrderService orderService;
    RoleRepository roleRepository;
    CustomerRepository customerRepository;
    OrderRepository orderRepository;
    MailSenderService mailSenderService;
    private final UserRepository userRepository;


    @Autowired
    public WebController (ProductService productService,
                          CategoryService categoryService,
                          ProducerService producerService,
                          CatalogService catalogService,
                          ProductDetailService productDetailService,
                          CartService cartService,
                          BannerService bannerService,
                          UserService userService,
                          RoleService roleService,
                          CustomerService customerService,
                          FileUploadService fileUploadService,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository,
                          CustomerRepository customerRepository,
                          OrderDetailService orderDetailService,
                          OrderService orderService,
                          OrderRepository orderRepository,
                          MailSenderService mailSenderService,
                          UserRepository userRepository){
        this.categoryService = categoryService;
        this.catalogService = catalogService;
        this.productService = productService;
        this.producerService = producerService;
        this.bannerService = bannerService;
        this.productDetailService = productDetailService;
        this.cartService = cartService;
        this.userService = userService;
        this.roleService = roleService;
        this.customerService = customerService;
        this.fileUploadService = fileUploadService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.mailSenderService = mailSenderService;
        this.userRepository = userRepository;
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
    @ModelAttribute("banners")
    public List<Banner> getAllBanners(){
        return bannerService.getAllBanner();
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
//    @ModelAttribute("avatar")
//    public String avatar(){
//        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)){
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            CustomizeUserDetails customizeUserDetails = (CustomizeUserDetails) authentication.getPrincipal();
//            return customizeUserDetails.getImg();
//        }else
//            return "";
//    }
    @GetMapping("")
    // to home page
    private String home(Model model){
        // get product with max discount
        List<ProductDetail> productDetails = productDetailService.getAll();
        productDetails.sort(Comparator.comparing(ProductDetail::getDiscount));
        ProductDetail maxOffer;
        maxOffer = productDetails.get(productDetails.size()-1);
        // get product with status Feature
        model.addAttribute("offer",maxOffer);
        model.addAttribute("phones",productService.getProductByCatIdAndStatus(1L, ProductStatus.Feature));
        model.addAttribute("laptops",productService.getProductByCatIdAndStatus(2L, ProductStatus.Feature));
        model.addAttribute("tablets",productService.getProductByCatIdAndStatus(3L, ProductStatus.Feature));
        model.addAttribute("watches",productService.getProductByCatIdAndStatus(4L, ProductStatus.Feature));
        model.addAttribute("accessories",productService.getProductByCatIdAndStatus(5L, ProductStatus.Feature));
        model.addAttribute("ears",productService.getProductByCatIdAndStatus(6L, ProductStatus.Feature));
        return "site/fragment/home";
    }
    @GetMapping("cart")
    // to cart item
    private String cart(Model model){
        return "site/fragment/cart";
    }
    @GetMapping("updateCart")
    // update cart item
    private String updateCart(@RequestParam(name = "productDetailId",required = false)Long productDetailId,
                              @RequestParam(name = "quantity") int quantity){

        cartService.update(productDetailId,quantity);
        return "redirect:/site/cart";
    }
    @GetMapping("removeCart/{productDetailId}")
    // remove cart item
    private String removeCart(@PathVariable("productDetailId")Long productDetailId){
        cartService.remove(productDetailId);
        return "redirect:/site/cart";
    }
    @GetMapping("checkout")
    // to check out page
    private String checkout(@AuthenticationPrincipal CustomizeUserDetails userDetails,Model model){
        // check if user is login
        if (userDetails != null){
            Customer customer = customerService.findByUSerId(userDetails.getUserId());
            // check if user has customer information
            if (customer != null){
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(new CustomerDTO(customer),orderDTO);
                model.addAttribute("order",orderDTO);
            }else{
                model.addAttribute("update","update");
                model.addAttribute("order",new OrderDTO());
            }
        }else {
            model.addAttribute("order",new OrderDTO());
        }
        return "site/fragment/checkout";
    }
    @GetMapping("findOrder")
    private String findOrder(Model model,@RequestParam(name = "orderCode",required = false)String orderCode){
        if (StringUtils.hasText(orderCode)){
            Order order = orderService.getByOrderCode(orderCode);
            if (order!= null)
                model.addAttribute("order",new OrderDTO(order));
        }else {
            model.addAttribute("order",null);
        }
        return "site/fragment/findOrder";
    }
    @PostMapping("order")
    // process order
    private String order(@AuthenticationPrincipal CustomizeUserDetails userDetails, Model model,
                        @Valid @ModelAttribute("order") OrderDTO dto,
                        BindingResult bindingResult, @RequestParam(name = "paymentMethod",required = false) String method) throws MessagingException {
        // check if cart is empty
        if (cartService.getCartItems().isEmpty())
            return "site/fragment/errorPage";
        //  check form's error
        if (bindingResult.hasErrors())
            return "site/fragment/checkout";
        // create order and save
        Order order = new Order();
        BeanUtils.copyProperties(dto,order);
        // generate order code
        String random = UUID.randomUUID().toString().trim().replace("-","").substring(0,4);
        String orderCode  = dto.getPhone()+random;
        order.setOrderCode(orderCode);
        if (method != null)
            order.setPaymentMethod(PaymentMethod.valueOf(method));
        order.setStatus(OrderStatus.Ordering);
        if (userDetails!= null)
            order.setUserId(userDetails.getUserId());
        double price = 0;
        if (cartService.getAmount()<50)
            price = cartService.getAmount()*1.1+10;
        else
            price = cartService.getAmount()*1.1;
        order.setTotalPrice(price);
        Order order1 = orderRepository.save(order);
        // create order detail and save
        Collection<CartItem> items = new HashSet<>(cartService.getCartItems());
        items.forEach(item -> {
            // save new order detail
            orderDetailService.save(OrderDetail.builder()
                    .productDetail(productDetailService.findById(item.getProductDetailId()))
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .order(order1)
                    .build());
        });
        model.addAttribute("order",new OrderDTO(orderService.getByOrderCode(orderCode)));
        model.addAttribute("items",items);
        model.addAttribute("price",price);
        model.addAttribute("orderCode",orderCode);
        // send mail
        String mailMessage = "<p>Your order <b>" + orderCode + "</b> has been created !!</p>";
        mailMessage += "<p>Save your order code to check order status.</p>";
        mailMessage += "<b>Sincere !!</b>";
        mailMessage += "<hr><img src='cid:logoImg' />";

        mailSenderService.sendOrderEmail(order1.getEmail(),"[MultiShop] Your order has been created",mailMessage);
        // clear cart item
        cartService.clear();
        return "site/fragment/orderSuccess";
    }
    @GetMapping("customer")
    // to customer page
    private String customer(@AuthenticationPrincipal CustomizeUserDetails userDetails,Model model){
        // get customer information
        Customer customer = customerService.findByUSerId(userDetails.getUserId());
        if (customer != null)
            model.addAttribute("customer",new CustomerDTO(customer));
        else
            model.addAttribute("customer",new CustomerDTO());
        // get orders of customer
        List<Order> list = orderService.getAllByUserId(userDetails.getUserId());
        List<OrderDTO> dtoList = new ArrayList<>();
        list.forEach(order -> {
            dtoList.add(new OrderDTO(order));
        });
        // sort returned list
        dtoList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        model.addAttribute("list",dtoList);
        model.addAttribute("username",userDetails.getUsername());
        model.addAttribute("email",userDetails.getEmail());
        return "site/fragment/customer/customer-infor";
    }
    @GetMapping("customer/update-password")
    public String updatePassword(@RequestParam("username") String username,Model model){
        model.addAttribute("username",username);
        return "site/fragment/customer/update-password";
    }
    @PostMapping("customer/update-password")
    public String doUpdatePassword(Model model, @RequestParam(name = "username",required = false) String username,
                                   @RequestParam(name = "oldPassword",required = false) String oldPassword,
                                   @RequestParam(name = "newPassword",required = false) String newPassword,
                                   @RequestParam(name = "confirmPassword",required = false) String confirmPassword){

        if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)
                || newPassword.length()<6 || newPassword.length()>20 || !confirmPassword.equals(oldPassword)){
            model.addAttribute("message","error");
            return "site/fragment/customer/update-password";
        }
        if (StringUtils.hasText(username)){
            User user = userService.getUserByUsername(username);
            if (user != null){
                if (passwordEncoder.matches(oldPassword,user.getPassword())){
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userService.save(user);
                    model.addAttribute("message","success");
                }else {
                    model.addAttribute("message","invalidOldPassword");
                }
            }else {
                return "site/fragment/errorPage";
            }
        }
        return "site/fragment/customer/update-password";
    }
    @PostMapping("customer/update")
    private String updateCustomer(Model model,@AuthenticationPrincipal CustomizeUserDetails userDetails,
                             @Valid @ModelAttribute("customer") CustomerDTO dto,
                             BindingResult  bindingResult,
                             @RequestParam("avatarImg")MultipartFile img) throws IOException {
        // check form error
        if (bindingResult.hasErrors()){
            model.addAttribute("username",userDetails.getUsername());
            return "site/fragment/customer/customer-infor";
        }
        // case user doesn't have customer information -> new
        if (dto.getCusId() == null){
            // check duplicate phone
            if (dto.getPhone()!=null)
                if (customerService.checkPhone(dto.getPhone())){
                    model.addAttribute("pMessage","Số điện thoại đã được sử dụng");
                    model.addAttribute("username",userDetails.getUsername());
                    return "site/fragment/customer/customer-infor";
                }
        }
        Customer customer = new Customer();
        // case user has customer information -> update
        if (dto.getCusId() != null){
            customer = customerRepository.getCustomerByUser_UserId(userDetails.getUserId());
        }
        // check duplicate phone
        if (!dto.getPhone().equals(customer.getPhone()))
            if (customerService.checkPhone(dto.getPhone())){
                model.addAttribute("eMessage","Phone đã được sử dụng");
                model.addAttribute("username",userDetails.getUsername());
                return "site/fragment/customer/customer-infor";
            }
        BeanUtils.copyProperties(dto,customer);
        // set user for customer
        customer.setUser(userService.getUserById(userDetails.getUserId()));
        // update avatar
        if (!img.isEmpty()){
            if (!Objects.equals(dto.getAvatarUrl(), ""))
                fileUploadService.deleteAvatar(dto.getAvatarUrl());
            customer.setAvatarUrl(fileUploadService.saveAvatar(img));
        }
        // save customer
        customerService.save(customer);
        return "redirect:/site/customer";
    }
    @PostMapping("customer/updateOrder/{orderId}")
    private String updateOrder(@PathVariable("orderId") Long orderId,
                               @RequestParam("cusName")String cusName,
                               @RequestParam("phone")String phone,
                               @RequestParam("address")String address,Model model){
        if (orderId != null){
            Order order = orderRepository.getReferenceById(orderId);
            if (order != null){
                order.setCusName(cusName);
                order.setPhone(phone);
                order.setAddress(address);
                orderService.save(order);
                model.addAttribute("message","Update success!!");
            }
        }

        return "redirect:/site/customer";
    }
    @GetMapping("customer/cancelOrder/{orderId}")
    private String cancelOrder(@PathVariable("orderId") Long orderId){
        if (orderId!=null){
            Order order = orderRepository.getReferenceById(orderId);
            if (order != null){
                order.setStatus(OrderStatus.Cancel);
                orderService.save(order);
            }
        }
        return "redirect:/site/customer";
    }
    @GetMapping("contact")
    private String contact(){
        return "site/fragment/contact";
    }
    @PostMapping("register")
    // register user
    private String register(Model model, HttpServletRequest request,
                            @RequestParam("rUsername") String username,
                            @RequestParam("rPassword") String password,
                            @RequestParam("rEmail") String email) throws MessagingException {
        // check information
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
            model.addAttribute("error","Tên đăng nhập đã tồn tại");
            return "/site/fragment/login";
        }
        if (userService.checkEmail(email)){
            model.addAttribute("error","Email đã được sử dụng");
            return "/site/fragment/login";
        }
        // create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        // none active
        user.setStatus(0);
        // generate verify code
        String verifyCode = RandomString.make(24);
        user.setVerifyCode(verifyCode);
        userService.save(user);
        // set role for new user, default role is ROLE_CUSTOMER
        Role role = new Role();
        role.setUser(userService.getUserByUsername(username));
        role.setAuthority(Authority.builder().authorityId(3L).authorityName("ROLE_CUSTOMER").build());
        roleService.saveRole(role);
        // generate active link
        String activeLink = Utility.getSiteUrl(request);
        activeLink += "/site/verify?code="+verifyCode;
        // generate mail content
        String mailMessage = "<p>Thank for your registration !!</p>";
        mailMessage += "<p>Click verify to active your account.</p>";
        mailMessage += "<a href=\"" + activeLink + "\">VERIFY</a><br>";
        mailMessage += "<b>Sincere !!</b>";
        mailMessage += "<hr><img src='cid:logoImg' />";
        // send mail
        mailSenderService.sendVerifyMail(email,"[MultiShop] Verification Email !!!",mailMessage);
        model.addAttribute("message","Your account has been create. Check your mail to active !!");
        return "/site/fragment/login";
    }
    @GetMapping("/verify")
    private String verify(@RequestParam(name = "code",required = false)String code,Model model){
        if (!code.isEmpty())
            if (userService.verify(code)){
                model.addAttribute("message","Xác thực thành công. Vui lòng đăng nhập.");
            }
        return "/site/fragment/login";
    }
    @GetMapping("/forget-password")
    private String forgetPassword(){
        return "/site/fragment/customer/reset-password";
    }
    @PostMapping("/verifyMail")
    private String resetPassword(Model model,@RequestParam("email") String email,HttpServletRequest request) throws MessagingException {
        if (userService.checkEmail(email)){
            User user = userRepository.getUsersByEmail(email).get();
            // generate verify link
            String verifyCode = RandomString.make(24);
            user.setResetPasswordCode(verifyCode);
            userRepository.save(user);
            String activeLink = Utility.getSiteUrl(request);
            activeLink += "/site/reset-password?code="+verifyCode;
            // generate mail content
            String mailMessage = "<p>Dear !!</p>";
            mailMessage += "<p>Click VERIFY below to reset password.</p>";
            mailMessage += "<a href=\"" + activeLink + "\">VERIFY</a><br>";
            mailMessage += "<b>Sincere !!</b>";
            mailMessage += "<hr><img src='cid:logoImg' />";
            mailSenderService.sendResetPasswordMail(email,mailMessage);
            model.addAttribute("message","verify");
        }else {
            model.addAttribute("message","false");
        }
        return "/site/fragment/customer/reset-password";
    }
    @GetMapping("/reset-password")
    private String doReset(Model model,@RequestParam("code")String code) throws MessagingException {
        User user = userService.getUserByResetPasswordCode(code);
        if (user != null){
            String newPassword = userService.resetPassword(user.getEmail());
            if (Objects.equals(newPassword, ""))
                return "/site/fragment/errorPage";
            else {
                // send mail
                String mailMessage = "<p>Your new password</p>";
                mailMessage += "<p><b>"+newPassword+"</b></p>";
                mailMessage += "<b>Sincere !!</b>";
                mailMessage += "<hr><img src='cid:logoImg' />";
                mailSenderService.sendEmail(user.getEmail(),"[MultiShopReset] Password Success",mailMessage);
                model.addAttribute("message","verifySuccess");
                return "/site/fragment/customer/reset-password";
            }
        }else {
            return "/site/fragment/errorPage";
        }
    }
    @GetMapping("detail/{proId}")
    // to detail page
    private String detail( Model model,
                           @PathVariable("proId") Long proId,
                           @RequestParam(name = "typeId", required = false)Long typeId){
        // get product by id
        model.addAttribute("product",productService.findById(proId));
        // get list detail of product
        List<ProductDetail> productDetails = productDetailService.getByProId(proId);
        // get list type of product
        if (!productDetails.isEmpty()){
            List<TypeDTO> types = new ArrayList<>();
            productDetails.forEach(productDetail -> {
                TypeDTO type = new TypeDTO();
                type.setTypeId(productDetail.getType().getTypeId());
                type.setTypeName(productDetail.getType().getTypeName());
                types.add(type);
            });
            // remove duplicate type
            HashSet<TypeDTO> typeDTOHashSet = new HashSet<>(types);
            // get list detail by typeId (list color)
            if (typeId != null){
                model.addAttribute("productDetails",productDetailService.getByProIdAndTypeId(proId,typeId));
            }else {
                // case typeId hasn't been chosen
                List<TypeDTO> list = new ArrayList<>(typeDTOHashSet);
                typeId = list.get(0).getTypeId();
                model.addAttribute("productDetails",productDetailService.getByProIdAndTypeId(proId,typeId));
            }
            model.addAttribute("currentId",productDetailService.getByProIdAndTypeId(proId,typeId).get(0).getProductDetailId());
            model.addAttribute("types",typeDTOHashSet);
            model.addAttribute("typeId",typeId);
            model.addAttribute("colorId",productDetailService.getByProIdAndTypeId(proId,typeId).get(0).getColor().getColorId());
            model.addAttribute("quantity",productDetailService.getByProIdAndTypeId(proId,typeId).get(0).getQuantity());
        }
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
