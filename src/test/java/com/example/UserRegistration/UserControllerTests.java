package com.example.UserRegistration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.UserRegistration.user.User;
import com.example.UserRegistration.user.UserController;
import com.example.UserRegistration.user.UserDTO;
import com.example.UserRegistration.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @MockBean
    UserService userService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void itShouldGetAllUsers() throws Exception{

        List<UserDTO> userDTOList = new ArrayList<>();
        UserDTO user1 = new UserDTO();
        user1.setUsername("test");
        user1.setEmail("test@gmail.com");
        UserDTO user2 = new UserDTO();
        user2.setUsername("test2");
        user2.setEmail("test2@gmail.com");
        userDTOList.add(user1);
        userDTOList.add(user2);

        ResponseEntity<List<UserDTO>> userDTOResponseEntity = new ResponseEntity<>(userDTOList, HttpStatus.OK);

        when(userService.getUsers()).thenReturn(userDTOResponseEntity);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(userDTOList.size())));
    }

    @Test
    public void itShouldRegisterUser() throws Exception{

        User user = new User("test","test","test@gmail.com");
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("test@gmail.com");

        ResponseEntity<UserDTO> userDTOResponseEntity = new ResponseEntity<>(userDTO, HttpStatus.CREATED);

        when(userService.registerUser(any())).thenReturn(userDTOResponseEntity);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", Matchers.is(userDTO.getUsername())));
    }

    @Test
    public void itShouldDeleteUser() throws Exception{

        Long id = 1L;

        when(userService.deleteUser(id)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete("/api/users/{id}", id)).andExpect(status().isOk());
    }

    @Test
    public void itShouldGetUserByEmail() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("test@gmail.com");

        when(userService.getbyEmail(userDTO.getEmail())).thenReturn(new ResponseEntity<>(userDTO, HttpStatus.OK));

        mockMvc.perform(get("/api/users/{email}", userDTO.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is(userDTO.getUsername())));
    }

    @Test
    public void itShouldGetUserByUsername() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("test@gmail.com");

        when(userService.getbyUsername(userDTO.getUsername())).thenReturn(new ResponseEntity<>(userDTO, HttpStatus.OK));

        mockMvc.perform(get("/api/users/findbyusername/{username}", userDTO.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is(userDTO.getUsername())));
    }
}
