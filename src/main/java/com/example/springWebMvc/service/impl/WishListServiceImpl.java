package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Product;
import com.example.springWebMvc.persistent.entities.User;
import com.example.springWebMvc.persistent.entities.WishList;
import com.example.springWebMvc.repository.ProductRepository;
import com.example.springWebMvc.repository.UserRepository;
import com.example.springWebMvc.repository.WishListRepository;
import com.example.springWebMvc.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishListServiceImpl implements WishListService {

    private WishListRepository repository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Autowired
    public WishListServiceImpl(WishListRepository repository,
                                UserRepository userRepository,
                                ProductRepository productRepository){
        this.repository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void save(Long userId, Long proId) {
        if (repository.getByUser_UserIdAndProduct_ProId(userId,proId) == null){
            WishList wishList = new WishList();
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(proId).orElse(null);
            if (user!=null && product!=null){
                wishList.setUser(user);
                wishList.setProduct(product);
                repository.save(wishList);
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (repository.existsById(id)){
            try {
                repository.deleteById(id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<WishList> getByUserId(Long userId) {
        return repository.getWishListsByUser_UserId(userId);
    }
}
