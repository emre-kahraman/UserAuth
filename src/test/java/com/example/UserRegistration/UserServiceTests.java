package com.example.UserRegistration;

import com.example.UserRegistration.dto.SignUpRequest;
import com.example.UserRegistration.entity.Role;
import com.example.UserRegistration.entity.User;
import com.example.UserRegistration.dto.UserDTO;
import com.example.UserRegistration.repository.RoleRepository;
import com.example.UserRegistration.repository.UserRepository;
import com.example.UserRegistration.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void itShouldReturnAllUsers(){

        List<User> userList = new ArrayList<>();
        User user1 = new User("test","test","test@gmail.com");
        User user2 = new User("test2","test2","test2@gmail.com");
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        ResponseEntity<List<UserDTO>> userDTOListResponseEntity = userService.getUsers();

        verify(userRepository).findAll();
        assertEquals(userDTOListResponseEntity.getBody().size(), userList.size());
        assertEquals(userDTOListResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void itShouldRegisterUser(){

        User user = new User("test","test","test@gmail.com");

        SignUpRequest signUpRequest = new SignUpRequest("test","test","test@gmail.com");

        when(userRepository.save(any())).thenReturn(user);

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.registerUser(signUpRequest);

        verify(userRepository).save(any());
        assertEquals(userDTOResponseEntity.getBody().getUsername(), user.getUsername());
        assertEquals(userDTOResponseEntity.getBody().getEmail(), user.getEmail());
        assertEquals(userDTOResponseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void itShouldDeleteUser(){

        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);

        doNothing().when(userRepository).deleteById(id);

        ResponseEntity<HttpStatus> httpStatusResponseEntity = userService.deleteUser(id);

        verify(userRepository).deleteById(id);
        verify(userRepository).existsById(id);
        assertEquals(httpStatusResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void itShouldGetUserByEmail(){

        String email = "test@gmail.com";
        User user = new User("test","test","test@gmail.com");

        when(userRepository.findByEmail(email)).thenReturn(user);

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.getbyEmail(email);

        verify(userRepository).findByEmail(email);
        assertEquals(userDTOResponseEntity.getBody().getUsername(), user.getUsername());
        assertEquals(userDTOResponseEntity.getBody().getEmail(), user.getEmail());
        assertEquals(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void itShouldGetUserByUsername(){

        String username = "test";
        User user = new User("test","test","test@gmail.com");

        when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.getbyUsername(username);

        verify(userRepository).findByUsername(username);
        assertEquals(userDTOResponseEntity.getBody().getUsername(), user.getUsername());
        assertEquals(userDTOResponseEntity.getBody().getEmail(), user.getEmail());
        assertEquals(userDTOResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void itShouldAddRoleToUser(){

        User user = new User("test","test","test@gmail.com");
        Role role = new Role("USER");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(roleRepository.findByName(role.getName())).thenReturn(role);

        ResponseEntity<UserDTO> userDTOResponseEntity = userService.addRoleToUser(user.getUsername(), role.getName());

        verify(userRepository).findByUsername(user.getUsername());
        verify(roleRepository).findByName(role.getName());
        assertEquals(userDTOResponseEntity.getBody().getUsername(), user.getUsername());
        assertEquals(userDTOResponseEntity.getBody().getRoles().size(), 1);
        assertEquals(userDTOResponseEntity.getBody().getRoles().contains(role), true);
    }
}
