package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Banner;

import java.util.List;

public interface BannerService {
    List<Banner> getAllBanner();
    Banner getBannerById(Long bannerId);
    boolean save(Banner banner);
}
