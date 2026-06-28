package com.srs.school.service;

import com.srs.school.dto.LoginResponse;
import com.srs.school.entity.User;
import com.srs.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public User saveUser(User user) {
        log.info("Entering saveUser - user: {}", user);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        log.info("Entering getAllUsers");
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        log.info("Entering getUserById - id: {}", id);
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public void deleteUser(String id) {
        log.info("Entering deleteUser - id: {}", id);
        userRepository.deleteById(id);
    }

    public User updateUser(String id, User user) {
        log.info("Entering updateUser - id: {}, user: {}", id, user);
        Optional<User> existing = userRepository.findById(id);
        if (existing.isPresent()) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * Authenticate user with username and password
     * @param username username of the user
     * @param password password of the user
     * @return LoginResponse with username, userId, schoolId, and message
     */
    public LoginResponse authenticate(String username, String password) {
        log.info("Entering authenticate - username: {}", username);
        LoginResponse response = new LoginResponse();
        
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(username, password);
        
        if (userOptional.isEmpty()) {
            response.setMessage("Invalid username or password");
            return response;
        }
        
        User user = userOptional.get();
        
        // Authentication successful
        response.setUsername(user.getUsername());
        response.setUserId(user.getId());
        response.setRole(user.getRole());
        if (user.getSchool() != null) {
            response.setSchoolId(user.getSchool().getId());
        }
        response.setMessage("Authentication successful");
        
        return response;
    }
}
