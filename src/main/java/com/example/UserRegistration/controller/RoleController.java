package com.example.UserRegistration.controller;

import com.example.UserRegistration.entity.Role;
import com.example.UserRegistration.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping()
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        return roleService.addRole(role);
    }
}
