package com.example.springWebMvc.controller.site;

import com.example.springWebMvc.persistent.*;
import com.example.springWebMvc.persistent.dto.*;
import com.example.springWebMvc.persistent.entities.*;
import com.example.springWebMvc.repository.CustomerRepository;
import com.example.springWebMvc.repository.OrderDetailRepository;
import com.example.springWebMvc.repository.RoleRepository;
import com.example.springWebMvc.service.*;
import com.example.springWebMvc.utility.OrderExcelExporter;
import com.example.springWebMvc.utility.Utility;
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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = {"/site"})
// http://localhost:8080/site
public class WebController {
    ProductService productService; ProducerService producerService; CategoryService categoryService;
    CatalogService catalogService; ProductDetailService productDetailService; CartService cartService;
    UserService userService; RoleService roleService; CustomerService customerService;
    FileUploadService fileUploadService; PasswordEncoder passwordEncoder; BannerService bannerService;
    OrderDetailService orderDetailService; OrderService orderService; OrderTrackingService trackingService;
    RoleRepository roleRepository; CustomerRepository customerRepository; MailSenderService mailSenderService;
    OrderDetailRepository orderDetailRepository; ReviewService reviewService; MessageService messageService;
    WishListService wishListService;

    @Autowired
    public WebController (ProductService productService, CategoryService categoryService, ProducerService producerService,
                          CatalogService catalogService, ProductDetailService productDetailService, CartService cartService,
                          BannerService bannerService, UserService userService, RoleService roleService,
                          CustomerService customerService, FileUploadService fileUploadService, PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository, CustomerRepository customerRepository, OrderDetailService orderDetailService,
                          OrderService orderService, OrderTrackingService trackingService, MailSenderService mailSenderService,
                          MessageService messageService, ReviewService reviewService, WishListService wishListService,
                          OrderDetailRepository orderDetailRepository){
        this.categoryService = categoryService; this.catalogService = catalogService; this.productService = productService;
        this.producerService = producerService; this.bannerService = bannerService; this.productDetailService = productDetailService;
        this.cartService = cartService; this.userService = userService; this.roleService = roleService;
        this.customerService = customerService; this.fileUploadService = fileUploadService; this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository; this.customerRepository = customerRepository; this.orderDetailService = orderDetailService;
        this.orderService = orderService; this.trackingService = trackingService; this.mailSenderService = mailSenderService;
        this.orderDetailRepository = orderDetailRepository; this.messageService = messageService; this.reviewService = reviewService;
        this.wishListService = wishListService;
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
    @ModelAttribute("wishList")
    public int getWishList(@AuthenticationPrincipal CustomizeUserDetails userDetails){
        if (userDetails != null){
            return wishListService.getByUserId(userDetails.getUserId()).size();
        }else
            return 0;
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
    public String home(Model model){
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
    public String cart(Model model){
        return "site/fragment/cart";
    }
    @GetMapping("updateCart")
    // update cart item
    public String updateCart(@RequestParam(name = "productDetailId",required = false)Long productDetailId,
                              @RequestParam(name = "quantity") int quantity){

        cartService.update(productDetailId,quantity);
        return "redirect:/site/cart";
    }
    @GetMapping("removeCart/{productDetailId}")
    // remove cart item
    public String removeCart(@PathVariable("productDetailId")Long productDetailId){
        cartService.remove(productDetailId);
        return "redirect:/site/cart";
    }
    @GetMapping("checkout")
    // to check out page
    public String checkout(@AuthenticationPrincipal CustomizeUserDetails userDetails,
                           @AuthenticationPrincipal CustomizeOAth2User oAth2User, Model model){
        // check if user is login
        Customer customer;
        Long userId;
        if (userDetails!=null){
            userId = userDetails.getUserId();
        }else if (oAth2User != null){
            userId = oAth2User.getUserId();
        }else {
            userId = null;
        }
        if (userId != null){
            customer = customerService.findByUSerId(userId);
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
    // to search order page
    public String findOrder(Model model,@RequestParam(name = "orderCode",required = false)String orderCode){
        if (StringUtils.hasText(orderCode)){
            Order order = orderService.getByOrderCode(orderCode);
            if (order!= null){
                List<OrderTrackingDTO> dtos = new ArrayList<>();
                if (!order.getOrderTracings().isEmpty()){
                    order.getOrderTracings().forEach(orderTracking -> dtos.add(new OrderTrackingDTO(orderTracking)));
                }
                model.addAttribute("order",new OrderDTO(order));
                model.addAttribute("track",dtos);
            }
        }else {
            model.addAttribute("order",null);
        }
        model.addAttribute("link","order");
        return "site/fragment/findOrder";
    }
    @GetMapping("export-csv/{orderCode}")
    // export to csv
    public void exportCSVOrder(Model model,
                               @PathVariable("orderCode") String code,
                               HttpServletResponse response) throws IOException {
        Order order = orderService.getByOrderCode(code);
        if (order != null){
            response.setContentType("text/csv");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            String fileName = currentDate + "_Order" + code;
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileName;
            response.setHeader(headerKey,headerValue);
            OrderDTO orderDTO = new OrderDTO(order);
            // create write
            ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            String[] orderInfor = {"Order Code","Name","Phone","Address","Email","Order time","Price"};
            String[] orderInforMapping = {"orderCode","cusName","phone","address","email","createTime","totalPrice"};
            String[] orderDetail = {"Product Name","Type","Color","Quantity"};
            String[] orderDetailMapping = {"productName","typeName","colorName","quantity"};
            csvBeanWriter.writeHeader(orderInfor);
            csvBeanWriter.write(orderDTO,orderInforMapping);
            csvBeanWriter.writeHeader(orderDetail);
            orderDTO.getOrderDetailDTOList().forEach(orderDetailDTO -> {
                try {
                    csvBeanWriter.write(orderDetailDTO.getProductDetailDTO(),orderDetailMapping);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvBeanWriter.close();
        }else {
            response.sendRedirect("../../errorPage");
        }
    }
    @GetMapping("export-excel/{orderCode}")
    // export to excel
    public void exportExcelOrder(@PathVariable("orderCode") String code,
                                 HttpServletResponse response) throws IOException {
        Order order = orderService.getByOrderCode(code);
        if (order != null){
            response.setContentType("application/octet-stream");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            String fileName = currentDate + "_Order" + code;
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileName + ".xlsx";
            response.setHeader(headerKey,headerValue);
            // create write
            OrderExcelExporter exporter = new OrderExcelExporter(new OrderDTO(order));
            exporter.export(response);
        }else {
            response.sendRedirect("../../errorPage");
        }
    }
    @PostMapping("order")
    // process order
    public String order(@AuthenticationPrincipal CustomizeUserDetails userDetails,
                        @AuthenticationPrincipal CustomizeOAth2User oAth2User,
                        Model model,
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
        // set user for order
        if (userDetails!= null)
            order.setUserId(userDetails.getUserId());
        if (oAth2User!=null)
            order.setUserId(oAth2User.getUserId());
        double price;
        if (cartService.getAmount()<50)
            price = cartService.getAmount() * Tax.tax10 + ShippingFee.Ship10;
        else
            price = cartService.getAmount() * Tax.tax10;
        order.setTotalPrice(price);
        Order order1 = orderService.save(order);
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
        // save order tracking
        trackingService.trackOrder(OrderStatus.Ordering, order1.getOrderId(),"Customer creates new order");

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
    public String customer(@AuthenticationPrincipal CustomizeUserDetails userDetails,
                           @AuthenticationPrincipal CustomizeOAth2User oAth2User, Model model){
        // get customer information
        Customer customer;
        List<Order> list;
        if (oAth2User!=null){
            User user = userService.getUserByEmail(oAth2User.getEmail());
            customer = customerService.findByUSerId(user.getUserId());
            list = orderService.getAllByUserId(user.getUserId());
            model.addAttribute("username",user.getUsername());
            model.addAttribute("email",user.getEmail());
        }else {
            customer = customerService.findByUSerId(userDetails.getUserId());
            list = orderService.getAllByUserId(userDetails.getUserId());
            model.addAttribute("username",userDetails.getUsername());
            model.addAttribute("email",userDetails.getEmail());
        }
        if (customer != null)
            model.addAttribute("customer",new CustomerDTO(customer));
        else
            model.addAttribute("customer",new CustomerDTO());
        // get orders of customer
        List<OrderDTO> dtoList = new ArrayList<>();
        list.forEach(order -> dtoList.add(new OrderDTO(order)));
        // sort returned list
        dtoList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        model.addAttribute("list",dtoList);
        return "site/fragment/customer/customer-infor";
    }
    @GetMapping("customer/update-password")
    // to update password page
    public String updatePassword(@RequestParam("username") String username,Model model){
        model.addAttribute("username",username);
        return "site/fragment/customer/update-password";
    }
    @PostMapping("customer/update-password")
    // do update password
    public String doUpdatePassword(Model model, @RequestParam(name = "username",required = false) String username,
                                   @RequestParam(name = "oldPassword",required = false) String oldPassword,
                                   @RequestParam(name = "newPassword",required = false) String newPassword,
                                   @RequestParam(name = "confirmPassword",required = false) String confirmPassword){

        if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)
                || newPassword.length()<6 || newPassword.length()>20 || !confirmPassword.equals(newPassword)){
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
    // update customer information
    public String updateCustomer(Model model,@AuthenticationPrincipal CustomizeUserDetails userDetails,
                             @AuthenticationPrincipal CustomizeOAth2User oAuth2User,
                             @Valid @ModelAttribute("customer") CustomerDTO dto,
                             BindingResult  bindingResult,
                             @RequestParam("avatarImg")MultipartFile img) throws IOException {
        User user  = new User();
        if (userDetails!=null)
            user = userService.getUserById(userDetails.getUserId());
        if (oAuth2User!=null)
            user = userService.getUserByEmail(oAuth2User.getEmail());
        // check form error
        if (bindingResult.hasErrors()){
            model.addAttribute("username",user.getUsername());
            return "site/fragment/customer/customer-infor";
        }
        // case user doesn't have customer information -> new
        if (dto.getCusId() == null){
            // check duplicate phone
            if (dto.getPhone()!=null)
                if (customerService.checkPhone(dto.getPhone())){
                    model.addAttribute("pMessage","Phone number has been used !!!");
                    model.addAttribute("username",user.getUsername());
                    model.addAttribute("email",user.getEmail());
                    return "site/fragment/customer/customer-infor";
                }
        }
        Customer customer = new Customer();
        // case user has customer information -> update
        if (dto.getCusId() != null){
            customer = customerService.findByUSerId(user.getUserId());
        }
        // check duplicate phone
        if (!dto.getPhone().equals(customer.getPhone()))
            if (customerService.checkPhone(dto.getPhone())){
                model.addAttribute("pMessage","Phone number has been used !!!");
                model.addAttribute("username",user.getUsername());
                model.addAttribute("email",user.getEmail());
                return "site/fragment/customer/customer-infor";
            }
        BeanUtils.copyProperties(dto,customer);
        // set user for customer
        customer.setUser(userService.getUserById(user.getUserId()));
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
    // update order
    public String updateOrder(@PathVariable("orderId") Long orderId,
                               @RequestParam("cusName")String cusName,
                               @RequestParam("phone")String phone,
                               @RequestParam("address")String address,Model model){
        if (orderId != null){
            Order order = orderService.getByOrderId(orderId);
            if (order != null){
                if (!order.getCusName().equals(cusName)&&
                        !order.getPhone().equals(phone)&&
                        !order.getAddress().equals(address))
                {
                    order.setCusName(cusName);
                    order.setPhone(phone);
                    order.setAddress(address);
                    order.setStatus(OrderStatus.Update);
                    orderService.save(order);

                    // save new order tracking
                    trackingService.trackOrder(OrderStatus.Update,orderId,"Customer updates shipping address");
                }
                model.addAttribute("message","Update success!!");
            }
        }
        return "redirect:/site/customer";
    }
    @GetMapping("customer/cancelOrder/{orderId}")
    // cancel order
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        if (orderId!=null){
            Order order = orderService.getByOrderId(orderId);
            if (order != null){
                order.setStatus(OrderStatus.Cancel);
                orderService.save(order);

                // save new order tracking
                trackingService.trackOrder(OrderStatus.Cancel,orderId,"Customer cancels order");
            }
        }
        return "redirect:/site/customer";
    }
    @GetMapping("contact")
    // to contact page
    public String contact(Model model,@AuthenticationPrincipal CustomizeUserDetails userDetails,
                          @AuthenticationPrincipal CustomizeOAth2User oAth2User){
        MessageDTO message = new MessageDTO();
        Long userId = null;
        if (userDetails!=null){
            userId = userDetails.getUserId();
        }
        if (oAth2User!=null){
            userId = oAth2User.getUserId();
        }
        if (userId != null){
            List<Message> list = messageService.getByUserId(userId);
            List<MessageDTO> dtos = new ArrayList<>();
            if (!list.isEmpty()){
                list.forEach(message1 -> dtos.add(new MessageDTO(message1)));
            }
            model.addAttribute("list",dtos);
            message.setCusEmail(userService.getUserById(userId).getEmail());
            if (customerService.findByUSerId(userId)!=null){
                message.setCusName(customerService.findByUSerId(userId).getCusName());
                message.setPhone(customerService.findByUSerId(userId).getPhone());
            }
        }
        model.addAttribute("newMessage",message);
        model.addAttribute("link","contact");
        return "site/fragment/contact";
    }
    @PostMapping("do-contact")
    // send contact message
    public String doContact(@Valid @ModelAttribute("newMessage") MessageDTO messageDTO,
                            BindingResult bindingResult,@AuthenticationPrincipal CustomizeUserDetails userDetails,
                            @AuthenticationPrincipal CustomizeOAth2User oAth2User){
        if (bindingResult.hasErrors()){
            return "site/fragment/contact";
        }
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO,message);
        message.setStatus(false);
        Long userId = null;
        if (userDetails!=null)
            userId = userDetails.getUserId();
        if (oAth2User!=null)
            userId = oAth2User.getUserId();
        if (userId!=null){
            message.setUser(userService.getUserById(userId));
        }
        messageService.save(message);
        return "redirect:/site/contact";
    }
    @PostMapping("do-review/{proId}")
    // send review
    public String doReview(@PathVariable("proId") Long proId,@AuthenticationPrincipal CustomizeUserDetails details,
                           @AuthenticationPrincipal CustomizeOAth2User oAth2User,
                           @RequestParam("rate") int rate,@Valid @ModelAttribute("review")ReviewDTO dto,
                           BindingResult result,Model model){
        if (result.hasErrors()){
            model.addAttribute("message","Invalid reviews");
            return "redirect:/site/detail/"+proId;
        }
        Review review = new Review();
        BeanUtils.copyProperties(dto,review);
        Long userId = null;
        if (details!=null)
            userId = details.getUserId();
        if (oAth2User!=null)
            userId = oAth2User.getUserId();
        if (userId!=null){
            review.setUser(userService.getUserById(userId));
        }
        review.setProduct(productService.findById(proId));
        review.setRate(rate);
        review.setStatus(false);
        reviewService.save(review);
        return "redirect:/site/detail/"+proId;
    }
    @PostMapping("register")
    // register user
    public String register(Model model, HttpServletRequest request,
                            @RequestParam("rUsername") String username,
                            @RequestParam("rPassword") String password,
                            @RequestParam("rEmail") String email) throws MessagingException {
        // check information
        if (username.length() < 3 || username.length() > 20)
        {
            model.addAttribute("uMessage","Username's length must be between 3 and 20 characters !!!");
            return "/site/fragment/login";
        }
        if (password.length() < 6 || password.length() > 20)
        {
            model.addAttribute("pMessage","Password's length must be between 6 and 20 characters !!!");
            return "/site/fragment/login";
        }
        if (userService.getUserByUsername(username) != null){
            model.addAttribute("error","Username has been used !!!");
            return "/site/fragment/login";
        }
        if (userService.checkEmail(email)){
            model.addAttribute("error","Email has been used !!!");
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
        model.addAttribute("message","Your account has been create. Check your mail to active !");
        return "/site/fragment/login";
    }
    @GetMapping("/verify")
    // verify account
    public String verify(@RequestParam(name = "code",required = false)String code,Model model){
        if (!code.isEmpty())
            if (userService.verify(code)){
                model.addAttribute("message","Verify successfully. Please login to continue !");
            }
        return "/site/fragment/login";
    }
    @GetMapping("/forget-password")
    // to forget password page
    public String forgetPassword(){
        return "/site/fragment/customer/reset-password";
    }
    @PostMapping("/verifyMail")
    // verify mail before resetting password
    public String resetPassword(Model model,@RequestParam("email") String email,HttpServletRequest request) throws MessagingException {
        if (userService.checkEmail(email)){
            User user = userService.getUserByEmail(email);
            // generate verify link
            String verifyCode = RandomString.make(24);
            user.setResetPasswordCode(verifyCode);
            userService.save(user);
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
    // reset password, send new password to mail
    public String doReset(Model model,@RequestParam("code")String code) throws MessagingException {
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
    public String detail( Model model,@AuthenticationPrincipal CustomizeUserDetails details,
                           @AuthenticationPrincipal CustomizeOAth2User oAth2User,
                           @PathVariable("proId") Long proId, Optional<String> message,
                           @RequestParam(name = "typeId", required = false)Long typeId){
        // fail review message
        message.ifPresent(s -> model.addAttribute("message", s));
        // get product by id
        model.addAttribute("product",productService.findById(proId));
        // create new review dto
        Long userId = null;
        String email = null;
        if (details!=null){
            email = details.getEmail();
            userId = details.getUserId();
        }
        if (oAth2User!=null){
            userId = oAth2User.getUserId();
            email = oAth2User.getEmail();
        }
        ReviewDTO reviewDTO = new ReviewDTO();
        if (userId!=null){
            if (customerService.findByUSerId(userId)!=null){
                reviewDTO.setCusName(customerService.findByUSerId(userId).getCusName());
            }
            reviewDTO.setCusEmail(email);
        }
        // get all reviews by productId
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        List<Review> reviews = reviewService.getByProIdAndStatus(proId,true);
        reviews.forEach(review -> {
            reviewDTOS.add(new ReviewDTO(review));
        });

        model.addAttribute("reviewDTO",reviewDTO);
        model.addAttribute("reviews",reviewDTOS);
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
            model.addAttribute("link","product");

            model.addAttribute("colorId",productDetailService.getByProIdAndTypeId(proId,typeId).get(0).getColor().getColorId());
            model.addAttribute("quantity",productDetailService.getByProIdAndTypeId(proId,typeId).get(0).getQuantity());
        }
        return "site/fragment/productDetail";
    }
    @GetMapping("products")
    // to all products page
    public String shop(Model model,
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
        model.addAttribute("link","product");

        return "site/fragment/allProducts";
    }
}
