package com.example.UserAuth.service;

import com.example.UserAuth.entity.Role;
import com.example.UserAuth.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteRole(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        roleRepository.delete(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
