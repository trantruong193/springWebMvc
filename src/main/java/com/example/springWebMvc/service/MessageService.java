package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAll();
    List<Message> getByStatus(boolean status);
    List<Message> getByUserId(Long id);
    boolean save (Message message);
    boolean delete (Long messageId);
    void checkMessage(Long messageId);
}
