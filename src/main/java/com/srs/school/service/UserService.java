package com.srs.school.service;

import com.srs.school.entity.User;
import com.srs.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User updateUser(String id, User user) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isPresent()) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }
}
