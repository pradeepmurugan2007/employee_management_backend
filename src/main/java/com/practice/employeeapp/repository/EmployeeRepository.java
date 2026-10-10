package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByEmail(String email);
    boolean existsByEmployeeCode(String employeeCode);
    List<Employee> findByStatus(String status);
    long countByStatus(String status);
    
    @org.springframework.data.jpa.repository.Query("SELECT e FROM Employee e WHERE (:status IS NULL OR :status = '' OR e.status = :status) AND (:search IS NULL OR :search = '' OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.departmentName) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Employee> searchEmployees(@org.springframework.data.repository.query.Param("status") String status, @org.springframework.data.repository.query.Param("search") String search);
}