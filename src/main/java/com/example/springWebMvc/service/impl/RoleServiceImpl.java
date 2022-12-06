package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Role;
import com.example.springWebMvc.repository.RoleRepository;
import com.example.springWebMvc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Override
    public boolean saveRole(Role role) {
        try {
            roleRepository.save(role);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
