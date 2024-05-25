package com.hubstaffmicroservices.tracktime.Services;
import com.hubstaffmicroservices.tracktime.Models.User;
import com.hubstaffmicroservices.tracktime.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User createUser(User user) {
        // Additional logic/validation can be added here
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            // Update the existing user with new data
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            // Save the updated user
            return userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}


