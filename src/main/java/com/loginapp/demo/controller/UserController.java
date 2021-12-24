package com.loginapp.demo.controller;

import com.loginapp.demo.dao.UserDao;
import com.loginapp.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    @GetMapping("/get")
    public List<User> getUsers() {
        List<User> users = userDao.findAll();
        return users;
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        User user = userDao.findByUsername(username);
        return user;
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        userDao.save(user);
        return user;
    }

    /*@PutMapping("/update/{username}")
    public User updateUserByUsername(@PathVariable String oldUsername) {
        User user = userDao.findByUsername(oldUsername);
        user.setUsername("mouslim");
        userDao.save(user);
        return user;
    }*/

}
