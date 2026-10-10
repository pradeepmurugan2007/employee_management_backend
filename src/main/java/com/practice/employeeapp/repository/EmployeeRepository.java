package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByEmail(String email);
    boolean existsByEmployeeCode(String employeeCode);
    List<Employee> findByStatus(String status);
    long countByStatus(String status);
}