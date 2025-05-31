package com.example.webapp.service;

import com.example.webapp.model.User;
import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User saveUser(User user) {
        if (user.getId() == null) {
            // New user - encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Existing user - only encode password if it has changed
            User existingUser = getUserById(user.getId());
            if (!user.getPassword().equals(existingUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User findByUsernameVulnerable(String username) {
        return userRepository.findByUsernameVulnerable(username);
    }
    
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return findByUsername(authentication.getName());
    }
    
    // Vulnerable deserialization
    public User deserializeUser(String serializedUser) {
        try {
            byte[] data = Base64.getDecoder().decode(serializedUser);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return (User) ois.readObject(); // Vulnerable to deserialization attacks
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String serializeUser(User user) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 