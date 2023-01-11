package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WishListRepository extends JpaRepository<WishList,Long> {
    List<WishList> getWishListsByUser_UserId(Long userId);
    WishList getByUser_UserIdAndProduct_ProId(Long userId,Long proId);
}
