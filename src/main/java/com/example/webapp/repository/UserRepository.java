package com.example.webapp.repository;

import com.example.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.webapp.model.Role;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // Safe method - use this for normal operations
    User findByUsername(String username);
    
    // Vulnerable to SQL Injection - for security testing only
    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    User findByUsernameVulnerable(@Param("username") String username);
    
    List<User> findByRole(Role role);
} 