package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Message;
import com.example.springWebMvc.repository.MessageRepository;
import com.example.springWebMvc.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getByUserId(Long id) {
        return messageRepository.getByUser_UserId(id);
    }

    @Override
    public List<Message> getByStatus(boolean status) {
        return messageRepository.getByStatus(status);
    }

    @Override
    public boolean save(Message message) {
        try {
            messageRepository.save(message);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(Long messageId) {
        try {
            messageRepository.deleteById(messageId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void checkMessage(Long messageId) {

        try {
            if (messageRepository.existsById(messageId)){
                Message message = messageRepository.getReferenceById(messageId);
                message.setStatus(true);
                messageRepository.save(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
