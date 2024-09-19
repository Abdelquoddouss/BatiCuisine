package Service;

import Entity.User;
import Repository.UserRepository;

import java.util.List;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create user
    public void addUser(User user) {
        userRepository.create(user);
    }

    // Get user by ID
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user
    public void updateUser(User user) {
        userRepository.update(user);
    }

    // Delete user
    public void deleteUser(int id) {
        userRepository.delete(id);
    }




}
