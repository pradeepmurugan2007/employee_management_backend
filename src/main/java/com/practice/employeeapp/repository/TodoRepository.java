package com.practice.employeeapp.repository;

import com.practice.employeeapp.models.User;
import com.practice.employeeapp.models.Todo;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


public interface TodoRepository extends JpaRepository <Todo, Integer> {

}


