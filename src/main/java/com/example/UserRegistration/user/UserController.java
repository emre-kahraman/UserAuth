package com.example.UserRegistration.user;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }


    @GetMapping("/listusers")
    public List<User> getUsers(){
        return userservice.getUsers();
    }

    @GetMapping("/findbyemail")
    public User getbyEmail(@RequestParam String email){
        return userservice.getbyEmail(email);
    }

    @GetMapping("findbyusername")
    public User getbyUsername(@RequestParam String username){
        return userservice.getbyUsername(username);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody User user) {

        userservice.registerUser(user);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam Long id){
        userservice.deleteUser(id);
    }


}
