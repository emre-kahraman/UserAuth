package com.example.UserAuth.controller;


import com.example.UserAuth.dto.AddRoleRequest;
import com.example.UserAuth.dto.SignUpRequest;
import com.example.UserAuth.dto.UserDTO;
import com.example.UserAuth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(){
        return userservice.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        return userservice.getUser(id);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email){
        return userservice.getbyEmail(email);
    }

    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username){
        return userservice.getbyUsername(username);
    }

    @PostMapping
    public ResponseEntity<UserDTO> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return userservice.registerUser(signUpRequest);
    }

    @PostMapping("/addRoleToUser")
    public ResponseEntity<UserDTO> addRoleToUser(@RequestBody AddRoleRequest addRoleRequest){
        return userservice.addRoleToUser(addRoleRequest.getUsername(), addRoleRequest.getRoleName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id){
        return userservice.deleteUser(id);
    }


}
