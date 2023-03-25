package com.example.UserAuth;

import com.example.UserAuth.entity.Role;
import com.example.UserAuth.entity.User;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RoleServiceIntegrationTests {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup(){
        roleRepository.deleteAll();
        Role role = new Role("USER");
        Role role2 = new Role("ADMIN");
        roleRepository.save(role);
        roleRepository.save(role2);
    }

    @Test
    public void itShouldAddRole(){

        Role role = new Role("TEST");

        ResponseEntity<Role> roleResponseEntity = roleService.addRole(role);

        Assertions.assertSame(roleResponseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertSame(roleResponseEntity.getBody().getName(), role.getName());
    }

    @Test
    public void itShouldGetRole(){

        ResponseEntity<Role> roleResponseEntity = roleService.getRole(1l);

        Assertions.assertSame(roleResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(roleResponseEntity.getBody().getName(), "USER");
    }

    @Test
    public void itShouldGetAllRoles(){

        ResponseEntity<List<Role>> listResponseEntity = roleService.getRoles();

        Assertions.assertSame(listResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(listResponseEntity.getBody().size(), 2);
    }

    @Test
    public void itShouldDeleteRoleByName(){

        String role = "USER";

        ResponseEntity<HttpStatus> roleResponseEntity = roleService.deleteRole(role);

        Assertions.assertSame(roleResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(roleRepository.findByName(role), Optional.empty());
    }
}
