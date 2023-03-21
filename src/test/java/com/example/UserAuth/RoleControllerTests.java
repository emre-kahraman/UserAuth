package com.example.UserAuth;

import com.example.UserAuth.controller.RoleController;
import com.example.UserAuth.entity.Role;
import com.example.UserAuth.service.RoleService;
import com.example.UserAuth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoleControllerTests {

    @MockBean
    private RoleService roleService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void itShouldAddRole() throws Exception {

        Role role = new Role("USER");

        when(roleService.addRole(any())).thenReturn(new ResponseEntity<>(role, HttpStatus.CREATED));

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is(role.getName())));
    }

    @Test
    @WithMockUser
    public void itShouldGetRole() throws Exception {

        Long id = 1l;
        Role role = new Role("USER");

        when(roleService.getRole(id)).thenReturn(new ResponseEntity<>(role, HttpStatus.OK));

        mockMvc.perform(get("/api/roles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(role.getName())));
    }

    @Test
    @WithMockUser
    public void itShouldGetRoles() throws Exception {

        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role("USER"));
        roleList.add(new Role("ADMIN"));

        when(roleService.getRoles()).thenReturn(new ResponseEntity<>(roleList, HttpStatus.OK));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(roleList.size())));
    }

    @Test
    @WithMockUser
    public void itShouldDeleteRole() throws Exception {

        Role role = new Role("USER");

        when(roleService.deleteRole(role.getName())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete("/api/roles/{name}", role.getName())).andExpect(status().isOk());
    }
}
