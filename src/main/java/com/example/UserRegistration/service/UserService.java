package com.example.UserRegistration.service;


import com.example.UserRegistration.dto.SignUpRequest;
import com.example.UserRegistration.entity.Role;
import com.example.UserRegistration.entity.User;
import com.example.UserRegistration.repository.RoleRepository;
import com.example.UserRegistration.repository.UserRepository;
import com.example.UserRegistration.dto.UserDTO;
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
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        users.stream().forEach(user -> userDTOList.add(convert(user)));
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> registerUser(SignUpRequest signUpRequest) {
        if(repo.findByUsername(signUpRequest.getUsername())!=null||repo.findByEmail(signUpRequest.getEmail())!=null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        String encryptedpassword = bCryptPasswordEncoder.encode(signUpRequest.getPassword());
        User savedUser = repo.save(new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encryptedpassword));
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
        User user = repo.findByEmail(email);
        if(user==null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        UserDTO userDTO = convert(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> getbyUsername(String username) {
        User user = repo.findByUsername(username);
        if(user==null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        UserDTO userDTO = convert(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> addRoleToUser(String username, String roleName){
        User user = repo.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        if(user==null||role==null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        user.getRoles().add(role);
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
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
        User user = repo.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("User not found");
        List<SimpleGrantedAuthority> authorities  = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}

