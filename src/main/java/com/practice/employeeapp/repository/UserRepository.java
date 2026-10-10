package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}