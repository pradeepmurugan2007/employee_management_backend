package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name);
}
