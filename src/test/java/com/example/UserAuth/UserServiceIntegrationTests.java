package com.example.UserAuth;

import com.example.UserAuth.dto.SignUpRequest;
import com.example.UserAuth.dto.UserDTO;
import com.example.UserAuth.entity.Role;
import com.example.UserAuth.entity.User;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.repository.UserRepository;
import com.example.UserAuth.service.UserService;
import org.junit.jupiter.api.Assertions;
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
@Transactional
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void itShouldReturnAllUsers(){

        User user1 = new User("test","test","test@gmail.com");
        User user2 = new User("test2","test2","test2@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        ResponseEntity<List<UserDTO>> userDTOListResponseEntity = userService.getUsers();

        Assertions.assertSame(userDTOListResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(userDTOListResponseEntity.getBody().size(), 2);
    }

    @Test
    public void itShouldRegisterUser(){

        SignUpRequest signUpRequest = new SignUpRequest("test","test","test@gmail.com");

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.registerUser(signUpRequest);

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertSame(userDTOResponseEntity.getBody().getUsername(), signUpRequest.getUsername());
        Assertions.assertSame(userDTOResponseEntity.getBody().getEmail(), signUpRequest.getEmail());
    }

    @Test
    public void itShouldAddRoleToUser(){

        User user = new User("test","test","test@gmail.com");
        Role role = new Role("USER");

        userRepository.save(user);
        roleRepository.save(role);

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.addRoleToUser(user.getUsername(), role.getName());

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(userDTOResponseEntity.getBody().getRoles().size(), 1);
    }

    @Test
    public void itShouldDeleteUser(){

        User user = new User("test","test","test@gmail.com");

        Long id = userRepository.save(user).getId();

        ResponseEntity<HttpStatus> httpStatusResponseEntity = userService.deleteUser(id);

        Assertions.assertSame(httpStatusResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(userRepository.findById(id), Optional.empty());
    }

    @Test
    public void itShouldGetUserByUsername(){

        User user = new User("test","test","test@gmail.com");

        userRepository.save(user);

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.getbyUsername(user.getUsername());

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(userDTOResponseEntity.getBody().getUsername().equals(user.getUsername()));
    }

    @Test
    public void itShouldGetUserByEmail(){

        User user = new User("test","test","test@gmail.com");

        userRepository.save(user);

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.getbyEmail(user.getEmail());

        Assertions.assertSame(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(userDTOResponseEntity.getBody().getEmail().equals(user.getEmail()));
    }
}
