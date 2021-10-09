package com.example.UserRegistration.user;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository repo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repo = repo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> getUsers(){
        return repo.findAll();
    }

    public void registerUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        if(repo.findByUsername(user.getUsername())==null&&repo.findByEmail(user.getEmail())==null) {
            String encryptedpassword = encoder.encode(user.getPassword());
            user.setPassword(encryptedpassword);
            repo.save(user);
        }
    }

    public void deleteUser(Long id) {
        if(repo.existsById(id))
            repo.deleteById(id);
    }


    public User getbyEmail(String email) {
        return repo.findByEmail(email);
    }

    public User getbyUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}

