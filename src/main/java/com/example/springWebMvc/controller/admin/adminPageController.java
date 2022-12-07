package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.entities.Banner;
import com.example.springWebMvc.service.BannerService;
import com.example.springWebMvc.service.FileUploadService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("admin")
// http://localhost:8080/admin
public class adminPageController {
    BannerService bannerService;
    FileUploadService fileUploadService;
    @Autowired
    public adminPageController(BannerService bannerService,FileUploadService fileUploadService){
        this.bannerService = bannerService;
        this.fileUploadService = fileUploadService;
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
        return "redirect:/admin/banner";
    }
}

