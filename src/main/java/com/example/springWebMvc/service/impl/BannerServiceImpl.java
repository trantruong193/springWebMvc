package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Banner;
import com.example.springWebMvc.repository.BannerRepository;
import com.example.springWebMvc.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    @Autowired
    public BannerServiceImpl(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }
    @Override
    public List<Banner> getAllBanner() {
        return bannerRepository.findAll();
    }

    @Override
    public Banner getBannerById(Long bannerId) {
        return bannerRepository.getBannerByBannerId(bannerId);
    }

    @Override
    public boolean save(Banner banner) {
        try {
            bannerRepository.save(banner);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
