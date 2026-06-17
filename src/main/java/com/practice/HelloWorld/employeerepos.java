package com.practice.HelloWorld;

import com.practice.HelloWorld.models.employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface employeerepos
        extends JpaRepository<employee, Integer> {

}