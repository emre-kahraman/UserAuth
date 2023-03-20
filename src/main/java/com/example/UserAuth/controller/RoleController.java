package com.example.UserAuth.controller;

import com.example.UserAuth.entity.Role;
import com.example.UserAuth.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id){
        return roleService.getRole(id);
    }

    @GetMapping()
    public ResponseEntity<List<Role>> getRoles(){
        return roleService.getRoles();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<HttpStatus> deleteRoleByName(@PathVariable String name){
        return roleService.deleteRole(name);
    }
}
