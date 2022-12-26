package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByProduct_ProIdAndStatus(Long proId,boolean status);
    List<Review> findByStatus(boolean status);
}
