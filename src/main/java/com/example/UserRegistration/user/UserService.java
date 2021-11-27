package com.example.UserRegistration.user;


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

    public List<UserDTO> getUsers(){
        List<User> users = repo.findAll();
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        users.stream().forEach(user -> userDTOList.add(convert(user)));
        return userDTOList;
    }

    public void registerUser(UserDTO userdto) {
        if(repo.findByUsername(userdto.getUsername())==null&&repo.findByEmail(userdto.getEmail())==null) {
            String encryptedpassword = bCryptPasswordEncoder.encode(userdto.getPassword());
            repo.save(new User(userdto.getUsername(), encryptedpassword, userdto.getEmail()));
        }
    }


    public void deleteUser(Long id) {
        if(repo.existsById(id))
            repo.deleteById(id);
    }


    public UserDTO getbyEmail(String email) {
        User user = repo.findByEmail(email);
        return convert(user);
    }

    public UserDTO getbyUsername(String username) {
        User user = repo.findByUsername(username);
        return convert(user);
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

