package chpt.cookbook.service;

import chpt.cookbook.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

}
