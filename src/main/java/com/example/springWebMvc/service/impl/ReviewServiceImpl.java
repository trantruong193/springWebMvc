package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Review;
import com.example.springWebMvc.repository.ReviewRepository;
import com.example.springWebMvc.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository repository;
    @Autowired
    public ReviewServiceImpl(ReviewRepository repository){
        this.repository = repository;
    }
    @Override
    public void save(Review review) {
        try {
            repository.save(review);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addReply(Long id, String reply) {
        if (repository.existsById(id)){
            Review review = repository.getReferenceById(id);
            if (reply != null){
                review.setShopReply(reply);
            }
            review.setStatus(true);
            repository.save(review);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (repository.existsById(id)){
                repository.deleteById(id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Review> getByProIdAndStatus(Long proId, boolean status) {
        return repository.findByProduct_ProIdAndStatus(proId,status);
    }

    @Override
    public List<Review> getByStatus(boolean status) {
        return repository.findByStatus(status);
    }
}
