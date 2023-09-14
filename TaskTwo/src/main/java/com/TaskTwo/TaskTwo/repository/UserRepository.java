package com.TaskTwo.TaskTwo.repository;

import com.TaskTwo.TaskTwo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String userName);


}
