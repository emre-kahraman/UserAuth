package com.example.UserAuth;

import com.example.UserAuth.entity.Role;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void itShouldAddRole(){

        Role role = new Role("USER");

        when(roleRepository.save(role)).thenReturn(role);

        ResponseEntity<Role> roleResponseEntity = roleService.addRole(role);

        verify(roleRepository).save(role);
        assertEquals(roleResponseEntity.getBody().getName(), role.getName());
        assertEquals(roleResponseEntity.getStatusCode(), HttpStatus.CREATED);
    }
}
