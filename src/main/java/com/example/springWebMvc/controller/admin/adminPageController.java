package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.OrderStatus;
import com.example.springWebMvc.persistent.dto.OrderDTO;
import com.example.springWebMvc.persistent.entities.*;
import com.example.springWebMvc.repository.ReviewRepository;
import com.example.springWebMvc.service.*;
import com.example.springWebMvc.test.ProRepo;
import com.example.springWebMvc.utility.OrderExcelExporter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.expression.Lists;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    public adminPageController(BannerService bannerService,
                               OrderService orderService,
                               FileUploadService fileUploadService,
                               OrderDetailService orderDetailService,
                               MessageService messageService,
                               ProductDetailService productDetailService,
                               ReviewService reviewService){
        this.bannerService = bannerService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.fileUploadService = fileUploadService;
        this.messageService = messageService;
        this.reviewService = reviewService;
        this.productDetailService = productDetailService;
    }
    @RequestMapping("")
    public String adminPage(Model model){
        model.addAttribute("banners",bannerService.getAllBanner());
        model.addAttribute("messages",messageService.getByStatus(false));
        model.addAttribute("reviews",reviewService.getByStatus(false));
        List<OrderDTO> orderDTOS = new ArrayList<>();
        List<Order> orders = orderService.findByStatus(OrderStatus.Ordering);
        orders.forEach(order -> {
            orderDTOS.add(new OrderDTO(order));
        });
        orderDTOS.sort(((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())));
        model.addAttribute("orders",orderDTOS);
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
        List<Order> orders = new ArrayList<>();
        if (orderId != null)
            orders = orderService.getAllByOrderId(orderId);
        else{
            if (phone == "")
                phone = null;
            if (date!=null) {
                // format date
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // get day
                String date1 = new String();
                date1 = format.format(date);
                // creat start date
                Date start  = format.parse(date1);
                // creat end date
                Date end = new Date(start.getTime()+86400000);

                if (phone!= null && status != null){
                    orders = orderService.findByPhoneStatusDate(phone,status,start,end);
                } else if (phone!= null && status == null) {
                    orders = orderService.findByPhoneDate(phone,start,end);
                }else if (phone == null && status != null) {
                    orders = orderService.findByStatusDate(status,start,end);
                }
                else {
                    orders = orderService.findByDate(start,end);
                }
            }else {
                if (phone!= null && status != null){
                    orders = orderService.findByPhoneStatus(phone,status);
                } else if (phone!= null && status == null) {
                    orders = orderService.getAllByPhone(phone);
                }else if (phone == null && status != null){
                    orders = orderService.findByStatus(status);
                }else {
                    orders = orderService.getAll();
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
                              @RequestParam("address") String address,Model model){
        if (orderId != null){
            Order order = orderService.getByOrderId(orderId);
            if (order != null){
                order.setCusName(cusName);
                order.setPhone(phone);
                order.setAddress(address);
                order.setStatus(status);
                orderService.save(order);
                if (status.equals(OrderStatus.Complete)){
                    order.getOrderDetails().forEach(orderDetail -> {
                        ProductDetail productDetail = orderDetail.getProductDetail();
                        productDetail.setQuantity(
                                Math.max((productDetail.getQuantity() - orderDetail.getQuantity()), 0)
                        );
                        productDetailService.save(productDetail);
                    });
                }
            }
        }
        return "redirect:/admin/order";
    }
    @GetMapping("cancelOrder/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        if (orderId!=null){
            Order order = orderService.getByOrderId(orderId);
            if (order != null){
                order.setStatus(OrderStatus.Cancel);
                orderService.save(order);
            }
        }
        return "redirect:/admin/order";
    }
    @GetMapping("updateOrderDetail/{detailId}")
    public String updateOrderDetail(@PathVariable("detailId") Long detailId,@RequestParam("quantity") int quantity){
        if (detailId != null){
            OrderDetail detail = orderDetailService.getById(detailId);
            if (detail != null){
                if (quantity!=0){
                    detail.setQuantity(quantity);
                    orderDetailService.save(detail);
                }else{
                    orderDetailService.delete(detailId);
                    Order order = orderService.getByOrderId(detail.getOrder().getOrderId());
                    if (order.getOrderDetails().isEmpty()){
                        order.setStatus(OrderStatus.Cancel);
                        orderService.save(order);
                    }
                }
            }
        }
        return "redirect:/admin/order";
    }
    @GetMapping("cancelOrderDetail/{orderDetailId}")
    public String cancelOrderDetail(@PathVariable("orderDetailId") Long orderDetailId){
        if (orderDetailId!=null){
            orderDetailService.delete(orderDetailId);
            Order order = orderService.getByOrderId(orderDetailService.getById(orderDetailId).getOrder().getOrderId());
            if (order.getOrderDetails().isEmpty()){
                order.setStatus(OrderStatus.Cancel);
                orderService.save(order);
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
}

