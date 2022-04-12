package com.example.UserRegistration.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers(){
        return userservice.getUsers();
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserDTO> getbyEmail(@PathVariable("email") String email){
        return userservice.getbyEmail(email);
    }

    @GetMapping("/users/findbyusername/{username}")
    public ResponseEntity<UserDTO> getbyUsername(@PathVariable("username") String username){
        return userservice.getbyUsername(username);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        return userservice.registerUser(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id){
        return userservice.deleteUser(id);
    }


}
