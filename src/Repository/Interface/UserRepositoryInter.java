package Repository.Interface;

import Entity.User;

import java.util.List;

public interface UserRepositoryInter {
    void create(User user);
    User findById(int id);
    List<User> findAll();
    void update(User user);
    void delete(int id);

}
