package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.entities.Banner;
import com.example.springWebMvc.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("admin")
// http://localhost:8080/admin
public class adminPageController {
    BannerService bannerService;
    @Autowired
    public adminPageController(BannerService bannerService){
        this.bannerService = bannerService;
    }
    @RequestMapping("")
    public String adminPage(){
        return "admin/fragment/adminHome";
    }
    @GetMapping("/banner")
    public String banner(Model model){
        model.addAttribute("banners",bannerService.getAllBanner());
        return "admin/fragment/webmanager/banner";
    }
    @GetMapping("/banner/edit/{bannerId}")
    public String edit(Model model,
                       @PathVariable("bannerId") Long bannerId){
        model.addAttribute("banner",bannerService.getBannerById(bannerId));
        return "admin/fragment/webmanager/edit";
    }
    @GetMapping("/banner/update")
    public String update(@Valid @ModelAttribute("banner") Banner banner, BindingResult bindingResult,
                         Model model,
                         @RequestParam("image") MultipartFile multipartFile){
        if (bindingResult.hasErrors())
            return "admin/fragment/webmanager/edit";

        bannerService.save(banner);
        model.addAttribute("message","Update successfully");
        return "admin/fragment/webmanager/edit";
    }
}

