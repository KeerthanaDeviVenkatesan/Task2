package com.TaskTwo.TaskTwo.service.Implements;

import com.TaskTwo.TaskTwo.entity.User;
import com.TaskTwo.TaskTwo.entity.UserInfo;
import com.TaskTwo.TaskTwo.repository.UserInfoRepository;
import com.TaskTwo.TaskTwo.repository.UserRepository;
import com.TaskTwo.TaskTwo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile(value = { "local", "dev", "prod" })
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;
    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //pagination
    public Page<User> findUserWithPagination(int offset, int pageSize){
        Page<User> users= repository.findAll(PageRequest.of(offset,pageSize));
        return users;
    }

    //sorting
    public List<User> findUserWithSorting(String field){
        return repository.findAll(Sort.by(Sort.Direction.ASC,field));
    }

    public List<User> getAllUser(){
        return repository.findAll();
    }

    @Cacheable(value = "user",key = "#id")
    public User getUserById(Long id){
        return repository.findById(id).orElse(null);
    }
    public User getUserByName(String userName){
        return repository.findByUserName(userName);
    }
    @CacheEvict(value = "userData", key = "#user.id")
    public User saveUser(User user){
        return repository.save(user);
    }
    @CachePut(value = "userData", key = "#user.id")
    public User updateUser(User user){
        return repository.save(user);
    }
    @CacheEvict(value = "userData", key = "#id")
    public void deleteUser(Long id){
        repository.deleteById(id);
    }

    public String addUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "user added to service";
    }

}
