package com.example.UserAuth.service;


import com.example.UserAuth.dto.SignUpRequest;
import com.example.UserAuth.entity.Role;
import com.example.UserAuth.entity.User;
import com.example.UserAuth.exception.EntityNotFoundException;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.repository.UserRepository;
import com.example.UserAuth.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository repo, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.repo = repo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<List<UserDTO>> getUsers(){
        List<User> users = repo.findAll();
        List<UserDTO> userDTOList = users.stream().map(this::convert).collect(Collectors.toList());
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> getUser(Long id){
        User user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        UserDTO userDTO = convert(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> registerUser(SignUpRequest signUpRequest) {
        if(!repo.findByUsername(signUpRequest.getUsername()).isEmpty() && !repo.findByEmail(signUpRequest.getEmail()).isEmpty())
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        String encryptedpassword = bCryptPasswordEncoder.encode(signUpRequest.getPassword());
        User savedUser = repo.save(new User(signUpRequest.getUsername(),encryptedpassword, signUpRequest.getEmail()));
        UserDTO userDTO = convert(savedUser);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }


    public ResponseEntity<HttpStatus> deleteUser(Long id) {
        if(!repo.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        repo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public ResponseEntity<UserDTO> getbyEmail(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
        UserDTO userDTO = convert(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> getbyUsername(String username) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found"));
        UserDTO userDTO = convert(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> addRoleToUser(String username, String roleName){
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role with name: " + roleName + " not found"));
        user.getRoles().add(role);
        User savedUser = repo.save(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(savedUser.getUsername());
        userDTO.setEmail(savedUser.getEmail());
        userDTO.setRoles(savedUser.getRoles());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    private UserDTO convert(User user){
        UserDTO userdto = new UserDTO();
        userdto.setUsername(user.getUsername());
        userdto.setEmail(user.getEmail());
        userdto.setRoles(user.getRoles());
        return userdto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<SimpleGrantedAuthority> authorities  = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}

