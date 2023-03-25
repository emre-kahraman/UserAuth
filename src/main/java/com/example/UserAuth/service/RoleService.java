package com.example.UserAuth.service;

import com.example.UserAuth.entity.Role;
import com.example.UserAuth.exception.EntityNotFoundException;
import com.example.UserAuth.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Role> addRole(Role role){
        if(!roleRepository.findByName(role.getName()).isEmpty())
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        Role savedRole = roleRepository.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    public ResponseEntity<Role> getRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with id: " + id + " not found"));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteRole(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role with name: " + name + " not found"));
        roleRepository.delete(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
