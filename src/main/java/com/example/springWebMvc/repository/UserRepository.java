package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> getUsersByUsername(String username);
    Optional<User> getUsersByEmail(String email);
    User getUserByVerifyCode(String code);
    User getUserByResetPasswordCode(String code);
}
