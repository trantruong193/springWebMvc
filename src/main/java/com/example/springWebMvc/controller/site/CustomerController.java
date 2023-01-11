package com.example.springWebMvc.controller.site;

import com.example.springWebMvc.persistent.dto.CustomizeUserDetails;
import com.example.springWebMvc.persistent.entities.WishList;
import com.example.springWebMvc.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CustomerController {
    WishListService wishListService;
    @Autowired
    public CustomerController(WishListService wishListService){
        this.wishListService = wishListService;
    }
    @GetMapping("/site/customer/wish-list")
    public String getWishList(Model model, @AuthenticationPrincipal CustomizeUserDetails userDetails){
        List<WishList> wishList = wishListService.getByUserId(userDetails.getUserId());
        model.addAttribute("list",wishList);
        return "site/fragment/customer/wish-list";
    }
    @GetMapping("/site/customer/add-wish-list")
    public String addWishList(@RequestParam(name = "proId") Long proId, HttpServletRequest request,
                              @AuthenticationPrincipal CustomizeUserDetails userDetails){
        if (proId != null && userDetails.getUserId() != null){
            wishListService.save(userDetails.getUserId(), proId);
        }
        String url = request.getHeader("referer");
        url = url.substring(22);
        return "redirect:/"+url;
    }
    @GetMapping("/site/customer/remove-wish-list/{id}")
    public String removeWishList(@PathVariable(name = "id")Long id){
        wishListService.delete(id);
        return "redirect:/site/customer/wish-list";
    }
}
