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
    public List<UserDTO> getUsers(){
        return userservice.getUsers();
    }

    @GetMapping("/findbyemail")
    public UserDTO getbyEmail(@RequestParam String email){
        return userservice.getbyEmail(email);
    }

    @GetMapping("findbyusername")
    public UserDTO getbyUsername(@RequestParam String username){
        return userservice.getbyUsername(username);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserDTO userdto) {
        userservice.registerUser(userdto);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam Long id){
        userservice.deleteUser(id);
    }


}
