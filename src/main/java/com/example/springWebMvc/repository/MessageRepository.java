package com.example.springWebMvc.repository;

import com.example.springWebMvc.persistent.entities.Message;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> getByUser_UserId(Long userId);
    List<Message> getByStatus(boolean status);
}
