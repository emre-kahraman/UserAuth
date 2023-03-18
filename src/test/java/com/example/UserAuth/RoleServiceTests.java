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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    public void itShouldGetAllRoles(){

        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role("USER"));
        roleList.add(new Role("ADMIN"));

        when(roleRepository.findAll()).thenReturn(roleList);

        ResponseEntity<List<Role>> listResponseEntity = roleService.getRoles();

        verify(roleRepository).findAll();
        assertEquals(listResponseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(listResponseEntity.getBody().size(), roleList.size());
        assertEquals(listResponseEntity.getBody(), roleList);
    }

    @Test
    public void itShouldDeleteRole(){

        Role role = new Role("USER");

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        doNothing().when(roleRepository).delete(role);

        ResponseEntity<HttpStatus> roleResponseEntity = roleService.deleteRole(role.getName());

        verify(roleRepository).delete(role);
        assertEquals(roleResponseEntity.getStatusCode(), HttpStatus.OK);
    }
}
