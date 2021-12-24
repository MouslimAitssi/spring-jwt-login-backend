package com.loginapp.demo.dao;

import com.loginapp.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Override
    User save(User user);
}
