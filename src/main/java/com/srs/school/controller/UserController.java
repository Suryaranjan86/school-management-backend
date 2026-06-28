package com.srs.school.controller;

import com.srs.school.dto.LoginRequest;
import com.srs.school.dto.LoginResponse;
import com.srs.school.entity.User;
import com.srs.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.info("Entering addUser - user: {}", user);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Entering getAllUsers");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        log.info("Entering getUserById - id: {}", id);
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Entering deleteUser - id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        log.info("Entering updateUser - id: {}, user: {}", id, user);
        User updated = userService.updateUser(id, user);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Login endpoint - authenticates user with username and password
     * Returns username and school_id on successful authentication
     * 
     * @param loginRequest containing username and password
     * @return LoginResponse with username, userId, schoolId, and authentication status message
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Entering login - username: {}", loginRequest != null ? loginRequest.getUsername() : null);
        LoginResponse response = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        
        // Check if authentication was successful by verifying if schoolId is set
        if (response.getSchoolId() != null && !response.getSchoolId().isEmpty()) {
            return ResponseEntity.ok(response);
        } else if (response.getUsername() != null && !response.getUsername().isEmpty()) {
            // User exists but school_id is null
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}

