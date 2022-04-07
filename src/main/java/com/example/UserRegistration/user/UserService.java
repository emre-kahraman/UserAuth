package com.example.UserRegistration.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository repo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repo = repo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<List<UserDTO>> getUsers(){
        List<User> users = repo.findAll();
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        users.stream().forEach(user -> userDTOList.add(convert(user)));
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> registerUser(User user) {
        if(repo.findByUsername(user.getUsername())!=null||repo.findByEmail(user.getEmail())!=null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        String encryptedpassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedpassword);
        User savedUser = repo.save(user);
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

    private UserDTO convert(User user){
        UserDTO userdto = new UserDTO();
        userdto.setUsername(user.getUsername());
        userdto.setEmail(user.getEmail());
        return userdto;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}

