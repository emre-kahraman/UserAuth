package com.example.UserAuth;

import com.example.UserAuth.dto.SignUpRequest;
import com.example.UserAuth.dto.UserDTO;
import com.example.UserAuth.entity.Role;
import com.example.UserAuth.entity.User;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.repository.UserRepository;
import com.example.UserAuth.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
        roleRepository.deleteAll();
        User user1 = new User("test","test","test@gmail.com");
        User user2 = new User("test2","test2","test2@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);
        Role role = new Role("USER");
        roleRepository.save(role);
    }

    @Test
    public void itShouldReturnAllUsers(){

        ResponseEntity<List<UserDTO>> userDTOListResponseEntity = userService.getUsers();

        Assertions.assertSame(userDTOListResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(userDTOListResponseEntity.getBody().size(), 2);
    }

    @Test
    @Order(1)
    public void itShouldGetUser(){

        Long id = userRepository.findByUsername("test").get().getId();

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.getUser(id);

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(userDTOResponseEntity.getBody().getUsername(), "test");
    }

    @Test
    public void itShouldRegisterUser(){

        SignUpRequest signUpRequest = new SignUpRequest("test3","test3@gmail.com", "test3");

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.registerUser(signUpRequest);

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertSame(userDTOResponseEntity.getBody().getUsername(), signUpRequest.getUsername());
        Assertions.assertSame(userDTOResponseEntity.getBody().getEmail(), signUpRequest.getEmail());
    }

    @Test
    public void itShouldAddRoleToUser(){

        String username = "test";
        String role = "USER";

        System.out.println(userRepository.findByUsername(username));

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.addRoleToUser(username, role);

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(userDTOResponseEntity.getBody().getRoles().size(), 1);
    }

    @Test
    public void itShouldDeleteUser(){

        Long id = userRepository.findByUsername("test").get().getId();

        ResponseEntity<HttpStatus> httpStatusResponseEntity = userService.deleteUser(id);

        Assertions.assertSame(httpStatusResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(userRepository.findById(1l), Optional.empty());
    }

    @Test
    public void itShouldGetUserByUsername(){

        String username = "test";

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.getbyUsername(username);

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(userDTOResponseEntity.getBody().getUsername().equals(username));
    }

    @Test
    public void itShouldGetUserByEmail(){

        String email = "test@gmail.com";

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.getbyEmail(email);

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(userDTOResponseEntity.getBody().getEmail().equals(email));
    }
}
