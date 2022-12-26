package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Review;

import java.util.List;

public interface ReviewService {
    void save(Review review);
    void delete(Long id);
    List<Review> getByProIdAndStatus(Long proId,boolean status);
    List<Review> getByStatus(boolean status);

    void addReply(Long id, String reply);

}
