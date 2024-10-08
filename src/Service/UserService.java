package Service;

import Entity.User;
import Repository.Interface.UserRepositoryInter;
import Repository.UserRepository;

import java.util.List;

public class UserService implements Service.Interface.UserServiceInter {

    private UserRepositoryInter userRepository;

    public UserService(UserRepositoryInter userRepository) {
        this.userRepository = userRepository;
    }

    // Create user
    @Override
    public void addUser(User user) {
        userRepository.create(user);
    }

    // Get user by ID
    public User getUserById(int id) {
        return userRepository.findById(id);
    }
    public User getUserByName(String nom) {
        return userRepository.findByName(nom);
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
