package com.TaskTwo.TaskTwo.service;

import com.TaskTwo.TaskTwo.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUser();

    User getUserById(Long id);
    User getUserByName(String userName);

    User updateUser(User user);
    void deleteUser(Long id);
}
