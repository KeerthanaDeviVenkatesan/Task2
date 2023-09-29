package com.TaskTwo.TaskTwo.controller;

import com.TaskTwo.TaskTwo.dto.APIResponse;
import com.TaskTwo.TaskTwo.dto.AuthRequest;
import com.TaskTwo.TaskTwo.entity.User;
import com.TaskTwo.TaskTwo.entity.UserInfo;
import com.TaskTwo.TaskTwo.service.Implements.UserServiceImpl;
import com.TaskTwo.TaskTwo.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskTwo")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserServiceImpl service;

    @Autowired
    JWTService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/{field}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public APIResponse<List<User>> getUserWithSorting(@PathVariable String field) {
        logger.info("Received request to get users with sorting by field: {}", field);
        List<User> allUsers = service.findUserWithSorting(field);
        logger.info("Returned {} users with sorting by field: {}", allUsers.size(), field);
        return new APIResponse<>(allUsers.size(), allUsers);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public APIResponse<Page<User>> getUserWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        logger.info("Received request to get users with pagination. Offset: {}, PageSize: {}", offset, pageSize);
        Page<User> userWithPagination = service.findUserWithPagination(offset, pageSize);
        logger.info("Returned {} users with pagination. Offset: {}, PageSize: {}", userWithPagination.getSize(), offset, pageSize);
        return new APIResponse<>(userWithPagination.getSize(), userWithPagination);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public User addUser(@RequestBody User user) {
        logger.info("Received request to add a new user: {}", user);
        User addedUser = service.saveUser(user);
        logger.info("Added new user with ID: {}", addedUser.getId());
        return addedUser;
    }


    @GetMapping("/userById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getUserId(@PathVariable Long id) {
        logger.info("Received request to get user by ID: {}", id);
        User user = service.getUserById(id);
        logger.info("Returned user by ID: {}", user);
        return user;
    }

    @GetMapping("/userByName/{userName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getUserName(@PathVariable String userName) {
        logger.info("Received request to get user by username: {}", userName);
        User user = service.getUserByName(userName);
        logger.info("Returned user by username: {}", user);
        return user;
    }

    @GetMapping("/allUsers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        logger.info("Received request to get all users.");
        List<User> users = service.getAllUser();
        logger.info("Returned {} users.", users.size());
        return users;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Received request to update user with ID: {}", id);
        user.setId(id);
        User updatedUser = service.updateUser(user);
        logger.info("Updated user with ID: {}", id);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void deleteUser(@PathVariable Long id) {
        logger.info("Received request to delete user with ID: {}", id);
        service.deleteUser(id);
        logger.info("Deleted user with ID: {}", id);
    }

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo){
        return service.addUser(userInfo);
    }
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUsername());
        }
        else
            throw new UsernameNotFoundException("Invalid user request");
    }
}
