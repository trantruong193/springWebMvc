package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Long> {
    Banner getBannerByBannerId(Long bannerId);
}
