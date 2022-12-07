package com.example.springWebMvc.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
// http://localhost:8080/admin
public class adminPageController {
    @RequestMapping("")
    public String adminPage(){
        return "admin/fragment/adminHome";
    }
    @GetMapping("/banner")
    public String banner(){
        return "admin/fragment/webmanager/banner";
    }
}
