package Service.Interface;

import Entity.User;

import java.util.List;

public interface UserServiceInter {
    void addUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(int id);
    User getUserByName(String nom);
}
