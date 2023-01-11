package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.ShippingFee;
import com.example.springWebMvc.persistent.Tax;
import com.example.springWebMvc.persistent.dto.*;
import com.example.springWebMvc.persistent.entities.*;
import com.example.springWebMvc.repository.RoleRepository;
import com.example.springWebMvc.service.*;
import com.example.springWebMvc.utility.OrderExcelExporter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
// http://localhost:8080/admin
public class adminPageController {
    BannerService bannerService;
    FileUploadService fileUploadService;
    OrderService orderService;
    OrderDetailService orderDetailService;
    MessageService messageService;
    ReviewService reviewService;
    ProductDetailService productDetailService;
    UserService userService;
    private final RoleRepository roleRepository;
    MailSenderService mailSenderService;
    OrderTrackingService trackingService;

    @Autowired
    public adminPageController(BannerService bannerService,
                               OrderService orderService,
                               FileUploadService fileUploadService,
                               OrderDetailService orderDetailService,
                               OrderTrackingService trackingService,
                               MessageService messageService,
                               ProductDetailService productDetailService,
                               UserService userService,
                               ReviewService reviewService,
                               RoleRepository roleRepository,
                               MailSenderService mailSenderService){
        this.bannerService = bannerService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.fileUploadService = fileUploadService;
        this.messageService = messageService;
        this.reviewService = reviewService;
        this.userService  = userService;
        this.trackingService = trackingService;
        this.productDetailService = productDetailService;
        this.roleRepository = roleRepository;
        this.mailSenderService = mailSenderService;
    }
    @RequestMapping("")
    public String adminPage(Model model){
        model.addAttribute("banners",bannerService.getAllBanner());
        model.addAttribute("messages",messageService.getByStatus(false));
        model.addAttribute("reviews",reviewService.getByStatus(false));
        model.addAttribute("link","home");
        // get ordering list
        List<OrderDTO> orderDTO = new ArrayList<>();
        List<Order> orderList = orderService.findByStatus(OrderStatus.Ordering);
        orderList.forEach(order -> {
            orderDTO.add(new OrderDTO(order));
        });
        // get recently update list
        List<OrderDTO> updateDTO = new ArrayList<>();
        List<Order> updateList = orderService.findByStatus(OrderStatus.Update);
        updateList.forEach(order -> updateDTO.add(new OrderDTO(order)));
        updateDTO.sort(((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())));
        // get order report in 30 day
        Date today = new Date();
        Date end = new Date(today.getTime()- 30L *86400000);
        List<Order> list30Days = orderService.findByDate(end,today);
        OrderReport report = new OrderReport(list30Days);
        model.addAttribute("orders",orderDTO);
        model.addAttribute("updates",updateDTO);
        model.addAttribute("report",report);
        return "admin/fragment/adminHome";
    }
    @GetMapping("/banner/edit/{bannerId}")
    public String edit(Model model,
                       @PathVariable("bannerId") Long bannerId){
        model.addAttribute("banner",bannerService.getBannerById(bannerId));
        return "admin/fragment/webmanager/edit";
    }
    @PostMapping("/banner/update")
    public String update(@Valid @ModelAttribute("banner") Banner banner,
                         Model model,
                         @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()){
            model.addAttribute("message","File is empty");
            return "admin/fragment/webmanager/edit";
        }
        Banner b = bannerService.getBannerById(banner.getBannerId());
        BeanUtils.copyProperties(banner,b);
        if (!multipartFile.isEmpty()){
            if (b.getImgUrl() != null)
                fileUploadService.delete(b.getImgUrl());
            banner.setImgUrl(fileUploadService.save(multipartFile));
        }
        bannerService.save(banner);
        model.addAttribute("message","Update successfully");
        return "redirect:/admin";
    }
    @GetMapping("order")
    public String getOrder (Model model,
                            @RequestParam(name = "orderId",required = false) Long orderId,
                            @RequestParam(name = "phone",required = false) String phone,
                            @RequestParam(name = "status",required = false) OrderStatus status,
                            @RequestParam(name = "date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
                            ) throws ParseException {
        orderService.getAll().forEach(order -> {
            if (order.getOrderDetails().isEmpty()){
                order.setStatus(OrderStatus.Cancel);
                orderService.save(order);
            }
        });
        List<Order> orders;
        if (orderId != null)
            orders = orderService.getAllByOrderId(orderId);
        else{
            if (Objects.equals(phone, ""))
                phone = null;
            if (date!=null) {
                // format date
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // get day
                String date1;
                date1 = format.format(date);
                // creat start date
                Date start  = format.parse(date1);
                // creat end date
                Date end = new Date(start.getTime()+86400000);

                if (phone!= null && status != null){
                    orders = orderService.findByPhoneStatusDate(phone,status,start,end);
                } else if (phone != null) {
                    orders = orderService.findByPhoneDate(phone,start,end);
                }else if (status != null) {
                    orders = orderService.findByStatusDate(status,start,end);
                }
                else {
                    orders = orderService.findByDate(start,end);
                }
            }else {
                if (phone!= null && status != null){
                    orders = orderService.findByPhoneStatus(phone,status);
                } else if (phone != null) {
                    orders = orderService.getAllByPhone(phone);
                }else if (status != null){
                    orders = orderService.findByStatus(status);
                }else {
                    Date today = new Date();
                    Date end = new Date(today.getTime() - 86400000*7);
                    orders = orderService.findByDate(end,today);
                }
            }
        }
        orders.sort(((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())));
        List<OrderDTO> lists = new ArrayList<>();
        orders.forEach(order -> {
            lists.add(new OrderDTO(order));
        });
        model.addAttribute("phone",phone);
        model.addAttribute("mstatus",status);
        model.addAttribute("date",date);
        model.addAttribute("list",lists);
        model.addAttribute("link","order");

        return "admin/fragment/order/list";
    }
    @GetMapping("exportExcelAll")
    public void exportAll(HttpServletResponse response) throws IOException {
        try {
            // get list new order
            List<OrderDTO> orderDTOS = new ArrayList<>();
            List<Order> orders = orderService.findByStatus(OrderStatus.Ordering);
            orders.forEach(order -> {
                orderDTOS.add(new OrderDTO(order));
            });
            orderDTOS.sort(((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())));
            // export to excel
            response.setContentType("application/octet-stream");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            String fileName = "New Orders" + currentDate;
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileName + ".xlsx";
            response.setHeader(headerKey,headerValue);
            // create write
            OrderExcelExporter exporter = new OrderExcelExporter(orderDTOS);
            exporter.exportAll(response);
        }catch (Exception e){
            response.sendRedirect("../../errorPage");
        }
    }
    @GetMapping("exportExcel/{orderCode}")
    public void exportExcel(HttpServletResponse response, @PathVariable(name = "orderCode",required = false) String code) throws IOException {
        if (code != null){
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
            }
        }else {
            response.sendRedirect("../../errorPage");
        }
    }
    @GetMapping("updateOrder/{orderId}")
    public String updateOrder(@PathVariable("orderId") Long orderId,
                              @RequestParam("cusName") String cusName,
                              @RequestParam("phone") String phone,
                              @RequestParam("status") OrderStatus status,
                              @RequestParam("address") String address,
                              @AuthenticationPrincipal CustomizeUserDetails userDetails){
        if (orderId != null){
            Order order = orderService.getByOrderId(orderId);
            if (order != null){
                order.setCusName(cusName);
                order.setPhone(phone);
                order.setAddress(address);
                if (!order.getStatus().equals(status)){
                    order.setStatus(status);
                }else {
                    order.setStatus(OrderStatus.Update);
                }
                orderService.save(order);

                // save new order tracking
                trackingService.trackOrder(status,orderId,"Admin " + userDetails.getUsername() + " updates order");

                if (status.equals(OrderStatus.Complete)){
                    order.getOrderDetails().forEach(orderDetail -> {
                        ProductDetail productDetail = orderDetail.getProductDetail();
                        productDetail.setQuantity(
                                Math.max((productDetail.getQuantity() - orderDetail.getQuantity()), 0)
                        );
                        productDetailService.save(productDetail);
                    });
                    trackingService.deleteTrackingOrder(orderId);
                }
            }
        }
        return "redirect:/admin/order";
    }
    @GetMapping("cancelOrder/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId,@AuthenticationPrincipal CustomizeUserDetails admin){
        if (orderId!=null){
            Order order = orderService.getByOrderId(orderId);
            if (order != null){
                order.setStatus(OrderStatus.Cancel);
                orderService.save(order);
                // save new order tracking
                trackingService.trackOrder(OrderStatus.Cancel,orderId,"Admin " + admin.getUsername() + " cancels order");
            }
        }
        return "redirect:/admin/order";
    }
    @GetMapping("updateOrderDetail/{detailId}")
    public String updateOrderDetail(@PathVariable("detailId") Long detailId,Model model,
                                    @RequestParam("quantity") int quantity,
                                    @AuthenticationPrincipal CustomizeUserDetails admin){
        if (quantity<0){
            model.addAttribute("error","Invalid data !!!");
            return "redirect:/admin/order";
        }
        if (detailId != null){
            OrderDetail detail = orderDetailService.getById(detailId);
            if (detail != null){
                Long orderId = detail.getOrder().getOrderId();
                if (quantity!=0){
                    // set new quantity for order detail
                    detail.setQuantity(quantity);
                    orderDetailService.save(detail);

                    // save new order tracking
                    String message = "Admin " + admin.getUsername() + " updates product quantity: "
                            + detail.getProductDetail().getProduct().getProName() + " to " + quantity;
                    trackingService.trackOrder(OrderStatus.Update,orderId,message);

                }else{
                    orderDetailService.delete(detailId);

                    // save new order tracking
                    trackingService.trackOrder(OrderStatus.Update,orderId,"Admin " + admin.getUsername() + " delete product: " +
                            detail.getProductDetail().getProduct().getProName() + " from order.");
                }
                // re-calculate total price
                Order order = orderService.getByOrderId(orderId);
                // set new status for order
                order.setStatus(OrderStatus.Update);

                if (!order.getOrderDetails().isEmpty()){
                    double price = order.getOrderDetails().stream().mapToDouble(item->(item.getQuantity()*item.getPrice())).sum();
                    if (price>50){
                        order.setTotalPrice(price * Tax.tax10);
                    }else {
                        order.setTotalPrice(price * Tax.tax10 + ShippingFee.Ship10);
                    }
                    orderService.save(order);
                }
                // save new order tracking
            }
        }
        return "redirect:/admin/order";
    }
    @GetMapping("cancelOrderDetail/{orderDetailId}")
    public String cancelOrderDetail(@PathVariable("orderDetailId") Long orderDetailId,
                                    @AuthenticationPrincipal CustomizeUserDetails admin){
        if (orderDetailId!=null){
            OrderDetail detail = orderDetailService.getById(orderDetailId);
            if (detail != null){
                Long orderId = detail.getOrder().getOrderId();
                orderDetailService.delete(orderDetailId);
                // re-calculate total price
                Order order = orderService.getByOrderId(orderId);
                if (!order.getOrderDetails().isEmpty()){
                    double price = order.getOrderDetails().stream().mapToDouble(item->(item.getQuantity()*item.getPrice())).sum();
                    if (price>50){
                        order.setTotalPrice(price * Tax.tax10);
                    }else {
                        order.setTotalPrice(price * Tax.tax10 + ShippingFee.Ship10);
                    }
                    order.setStatus(OrderStatus.Update);
                    orderService.save(order);
                }
                // save new order tracking
                trackingService.trackOrder(OrderStatus.Update,orderId,"Admin " + admin.getUsername() + " delete product: " +
                        detail.getProductDetail().getProduct().getProName() + " from order.");
            }
        }
        return "forward:/admin/order";
    }
    @GetMapping("check-message/{id}")
    public String checkMessage(@PathVariable("id") Long id){
        messageService.checkMessage(id);
        return "redirect:/admin";
    }
    @GetMapping("delete-review/{id}")
    public String deleteReview(@PathVariable Long id){
        reviewService.delete(id);
        return "redirect:/admin";
    }
    @GetMapping("reply-review/{id}")
    public String replyReview(@PathVariable("id") Long id,@RequestParam(name = "reply",required = false) String reply){
        reviewService.addReply(id,reply);
        return "redirect:/admin";
    }
    @GetMapping("/account")
    public String userAccount(Model model,@RequestParam(name = "email",required = false)String email){
        User user;
        Customer customer;
        if (email != null){
            user = userService.getUserByEmail(email);
            if (user!=null){
                model.addAttribute("user",new UserDTO(user));
                if (user.getCustomer()!=null){
                    customer = user.getCustomer();
                    model.addAttribute("customer",new CustomerDTO(customer));
                }
            }
        }
        model.addAttribute("link","account");

        return "admin/fragment/webmanager/customer-infor";
    }
    @GetMapping("/account/update/{userId}")
    public String updateAccount(@PathVariable("userId") Long userId,
                                @RequestParam("role") Long role,
                                @RequestParam("status") int status){
        User user = userService.getUserById(userId);
        List<Long> list = new ArrayList<>();
        if (!user.getRoles().isEmpty()){
            user.getRoles().forEach(r -> list.add(r.getAuthority().getAuthorityId()));
            if (!list.contains(role)){
                Role role1 = new Role();
                role1.setUser(user);
                role1.setAuthority(Authority.builder()
                                .authorityId(role)
                                .authorityName(role==2?"ROLE_CUSTOMER":"ROLE_ADMIN")
                        .build());
                roleRepository.save(role1);
            }
        }
        user.setStatus(status);
        userService.save(user);
        return "redirect:/admin/account?email="+user.getEmail();
    }
    @GetMapping("/account/reset-password")
    public String updateAccount(@RequestParam("username") String username,Model model) throws MessagingException {
        User user = userService.getUserByUsername(username);
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
                model.addAttribute("message","Reset success.Please check email");
                return "redirect:/admin/account?email="+user.getEmail();
            }
        }else {
            return "/site/fragment/errorPage";
        }
    }
}

