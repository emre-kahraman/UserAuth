package com.example.UserAuth;

import com.example.UserAuth.entity.Role;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class RoleServiceIntegrationTests {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void itShouldAddRole(){

        Role role = new Role("USER");

        ResponseEntity<Role> roleResponseEntity = roleService.addRole(role);

        Assertions.assertSame(roleResponseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertSame(roleResponseEntity.getBody().getName(), role.getName());
    }

    @Test
    public void itShouldGetAllRoles(){

        Role role = new Role("USER");
        Role role2 = new Role("ADMIN");

        roleRepository.save(role);
        roleRepository.save(role2);

        ResponseEntity<List<Role>> listResponseEntity = roleService.getRoles();

        Assertions.assertSame(listResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(listResponseEntity.getBody().size(), 2);
    }

    @Test
    public void itShouldDeleteRoleByName(){

        Role role = new Role("USER");

        roleRepository.save(role);

        ResponseEntity<HttpStatus> roleResponseEntity = roleService.deleteRole(role.getName());

        Assertions.assertSame(roleResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertSame(roleRepository.findByName(role.getName()), null);
    }
}
