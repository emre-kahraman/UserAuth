package com.example.UserRegistration.service;

import com.example.UserRegistration.entity.Role;
import com.example.UserRegistration.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Role> addRole(Role role){
        if(roleRepository.findByName(role.getName())!=null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        Role savedRole = roleRepository.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }
}
